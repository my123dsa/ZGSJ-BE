from kafka import KafkaConsumer
from confluent_kafka import Producer
import json
import mysql.connector
from mysql.connector import Error
import time
import logging
import threading
import base64

logging.basicConfig(
  level=logging.INFO,
  format='%(asctime)s %(levelname)s %(message)s'
)
logger = logging.getLogger(__name__)

def connect_to_database():
  try:
      connection = mysql.connector.connect(
          host="mysql",
          port=3306,
          database="attendance",
          user="root",
          password="1234"
      )
      logger.info("Successfully connected to MySQL database")
      return connection
  except Error as e:
      logger.error(f"Error connecting to database: {e}")
      return None

def create_producer():
    producer = Producer({
        'bootstrap.servers': 'kafka-service:9092',
        'acks': 'all'  # 모든 브로커가 메시지를 받은 후 전송 완료로 처리
    })
    return producer

def handle_message(msg_value):
  if not msg_value:
      logger.warning("Received empty message")
      return None
  try:
      if isinstance(msg_value, bytes):
          msg_value = msg_value.decode('utf-8')
      return json.loads(msg_value) if isinstance(msg_value, str) else msg_value
  except Exception as e:
      logger.error(f"Error decoding message: {e}")
      return None

def handle_president_changes(cursor, operation, data):
   try:
       sql = None
       values = None

       if operation == 'c':
           sql = """INSERT INTO president
                   (president_id, name, email, terms_accept,version)
                   VALUES (%s, %s, %s, %s, %s)"""
           values = (data['after']['president_id'],
                    data['after']['name'],
                    data['after']['email'],
                    data['after']['terms_accept'],
                    data['after']['version'])
       elif operation == 'u':
           sql = """UPDATE president
                   SET name = %s,
                       email = %s,
                       terms_accept = %s,
                       version = %s
                   WHERE president_id = %s"""
           values = (data['after']['name'],
                    data['after']['email'],
                    data['after']['terms_accept'],
                    data['after']['version'],
                    data['after']['president_id'])
       elif operation == 'd':
           sql = """DELETE FROM president WHERE president_id = %s"""
           values = (data['before']['president_id'],)

       if sql and values:
           cursor.execute(sql, values)
           logger.info(f"President table - Executed {operation} operation: {values}")
           return True
       else:
           logger.error(f"Unsupported operation: {operation}")
           return False

   except Exception as e:
       logger.error(f"Error handling President change: {e}")
       return False

def handle_store_changes(cursor, operation, data):
    try:
        sql = None
        values = None

        if operation == 'c':
            sql = """INSERT INTO store
                   (store_id, store_name, account_number, bank_code,
                    location, latitude, longitude, president_id, version)
                   VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)"""

            try:
                lat_bytes = base64.b64decode(data['after']['latitude'])
                long_bytes = base64.b64decode(data['after']['longitude'])

                latitude = round(int.from_bytes(lat_bytes, byteorder='big') / 10000000, 7)
                longitude = round(int.from_bytes(long_bytes, byteorder='big') / 10000000, 7)

                logger.info(f"Decoded coordinates: lat={latitude}, long={longitude}")
            except Exception as e:
                logger.error(f"Error decoding coordinates: {e}")
                return False

            values = (data['after']['store_id'],
                     data['after']['store_name'],
                     data['after']['account_number'],
                     data['after']['bank_code'],
                     data['after']['location'],
                     latitude,
                     longitude,
                     data['after']['president_id'],
                     data['after']['version'])

        elif operation == 'u':
            sql = """UPDATE store
                   SET store_name = %s,
                       account_number = %s,
                       bank_code = %s,
                       location = %s,
                       latitude = %s,
                       longitude = %s,
                       president_id = %s,
                       version = %s
                   WHERE store_id = %s"""

            try:
                lat_bytes = base64.b64decode(data['after']['latitude'])
                long_bytes = base64.b64decode(data['after']['longitude'])

                latitude = round(int.from_bytes(lat_bytes, byteorder='big') / 10000000, 7)
                longitude = round(int.from_bytes(long_bytes, byteorder='big') / 10000000, 7)

                logger.info(f"Decoded coordinates: lat={latitude}, long={longitude}")
            except Exception as e:
                logger.error(f"Error decoding coordinates: {e}")
                return False

            values = (data['after']['store_name'],
                     data['after']['account_number'],
                     data['after']['bank_code'],
                     data['after']['location'],
                     latitude,
                     longitude,
                     data['after']['president_id'],
                     data['after']['version'],
                     data['after']['store_id'])

        elif operation == 'd':
            sql = """DELETE FROM store WHERE store_id = %s"""
            values = (data['before']['store_id'],)

        if sql and values:
            cursor.execute(sql, values)
            logger.info(f"Store table - Executed {operation} operation: {values}")
            return True
        else:
            logger.error(f"Unsupported operation: {operation}")
            return False

    except Exception as e:
        logger.error(f"Error handling Store change: {e}")
        logger.error(f"Complete error data: {data}")
        return False

def handle_store_employee_changes(cursor, operation, data):
    try:
        sql = None
        values = None
        def convert_to_date(days):
            from datetime import datetime, timedelta
            base_date = datetime(1900, 1, 1)
            try:
                return (base_date + timedelta(days=int(days))).strftime('%Y-%m-%d')
            except (ValueError, TypeError):
                logger.error(f"Invalid birth_date value: {days}")
                return None

        if operation == 'c':
            sql = """INSERT INTO store_employee
                    (se_id, store_id, email, name, salary, employment_type,
                     bank_code, account_number, payment_date, birth_date, phone_number, version)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"""
            try:
                salary = int(data['after']['salary'])
                birth_date = convert_to_date(data['after']['birth_date'])
                if not birth_date:
                    return False
            except (ValueError, TypeError):
                logger.error(f"Invalid salary value: {data['after']['salary']}")
                return False

            logger.info(f"Received data: {data['after']}")

            values = (data['after']['se_id'],
                     data['after']['store_id'],
                     data['after']['email'],
                     data['after']['name'],
                     salary,
                     data['after']['employment_type'],
                     data['after']['bank_code'],
                     data['after']['account_number'],
                     data['after']['payment_date'],
                     birth_date,
                     data['after']['phone_number'],
                     data['after']['version'])

        elif operation == 'u':
            sql = """UPDATE store_employee
                    SET store_id = %s,
                        email = %s,
                        name = %s,
                        salary = %s,
                        employment_type = %s,
                        bank_code = %s,
                        account_number = %s,
                        payment_date = %s,
                        birth_date = %s,
                        phone_number = %s,
                        version = %s
                    WHERE se_id = %s"""
            try:
                salary = int(data['after']['salary'])
                birth_date = convert_to_date(data['after']['birth_date'])
                if not birth_date:
                    return False
            except (ValueError, TypeError):
                logger.error(f"Invalid salary value: {data['after']['salary']}")
                return False

            logger.info(f"Received data: {data['after']}")

            values = (data['after']['store_id'],
                     data['after']['email'],
                     data['after']['name'],
                     salary,
                     data['after']['employment_type'],
                     data['after']['bank_code'],
                     data['after']['account_number'],
                     data['after']['payment_date'],
                     birth_date,
                     data['after']['phone_number'],
                     data['after']['version'],
                     data['after']['se_id'])

        elif operation == 'd':
            sql = """DELETE FROM store_employee WHERE se_id = %s"""
            values = (data['before']['se_id'],)

        if sql and values:
            cursor.execute(sql, values)
            logger.info(f"Store Employee table - Executed {operation} operation: {values}")
            return True
        else:
            logger.error(f"Unsupported operation: {operation}")
            return False

    except Exception as e:
        logger.error(f"Error handling Store Employee change: {e}")
        return False


def process_messages_before(topic, handler_func):
  while True:
      try:
          connection = connect_to_database()
          if not connection:
              time.sleep(5)
              continue

          consumer = KafkaConsumer(
              topic,
              bootstrap_servers=['kafka-service:9092'],
              auto_offset_reset='earliest',
#               enable_auto_commit=True,
              enable_auto_commit=False, # 자동 커밋 비활성화
              group_id=f'{topic.replace(".", "_")}_sync_group',
              value_deserializer=None # 메시지를 bytes로 받아 직접 handle_message에서 처리
          )

          logger.info(f"Started consuming messages from topic: {topic}")

          for message in consumer:
              try:
                  decoded_message = handle_message(message.value)
                  if not decoded_message or 'payload' not in decoded_message:
                      continue

                  cursor = connection.cursor()
                  data = decoded_message['payload']
                  operation = data['op']

                  if handler_func(cursor, operation, data):
                      connection.commit()
                      consumer.commit()  # 수동으로 오프셋 커밋
                  else:
                      connection.rollback()
                      consumer.commit()  # 수동으로 오프셋 커밋

                  cursor.close()
              except Exception as e:
                  logger.error(f"Error processing message from {topic}: {e}")
                  if 'cursor' in locals():
                      cursor.close()
                  connection.rollback()

      except Exception as e:
          logger.error(f"Error in {topic} consumer: {e}")
          if 'connection' in locals() and connection:
              connection.close()
          time.sleep(5)

def main():
   logger.info("Starting sync service...")

   president_topic = 'mysql-president.member.president'
   store_topic = 'mysql-store.member.store'
   store_employee_topic = 'mysql-store-employee.member.store_employee'

   logger.info(f"Subscribing to topics: {president_topic}, {store_topic}, and {store_employee_topic}")

   try:
       temp_consumer = KafkaConsumer(
           bootstrap_servers=['kafka-service:9092']
       )
       existing_topics = temp_consumer.topics()
       logger.info(f"Available topics: {existing_topics}")
       temp_consumer.close()
   except Exception as e:
       logger.error(f"Error checking topics: {e}")

   president_thread = threading.Thread(
       target=process_messages,
#        target=process_messages_before,
       args=(president_topic, handle_president_changes)
   )

   store_thread = threading.Thread(
       target=process_messages,
#        target=process_messages_before,
       args=(store_topic, handle_store_changes)
   )

   store_employee_thread = threading.Thread(
       target=process_messages,
#        target=process_messages_before,
       args=(store_employee_topic, handle_store_employee_changes)
   )


# 추가
   president_dlq_thread = threading.Thread(
       target=process_dlq_messages,
       args=(f"dlq.{president_topic}", handle_president_changes)
   )

   store_dlq_thread = threading.Thread(
       target=process_dlq_messages,
       args=(f"dlq.{store_topic}", handle_store_changes)
   )

   store_employee_dlq_thread = threading.Thread(
       target=process_dlq_messages,
       args=(f"dlq.{store_employee_topic}", handle_store_employee_changes)
   )
   president_dlq_thread.start()
   store_dlq_thread.start()
   store_employee_dlq_thread.start()

   president_thread.start()
   store_thread.start()
   store_employee_thread.start()

   try:
       president_thread.join()
       store_thread.join()
       store_employee_thread.join()

       president_dlq_thread.join()
       store_dlq_thread.join()
       store_employee_dlq_thread.join()
   except KeyboardInterrupt:
       logger.info("Shutting down...")

# dlq 처리

MAX_RETRIES = 3
RETRY_DELAY = 2  # seconds

def process_messages(topic, handler_func):
    while True:
        try:
            connection = connect_to_database()
            if not connection:
                time.sleep(5)
                continue

            consumer = KafkaConsumer(
                topic,
                bootstrap_servers=['kafka-service:9092'],
                auto_offset_reset='earliest',
                enable_auto_commit=False,
                group_id=f'{topic.replace(".", "_")}_sync_group',
                value_deserializer=None
            )

            producer = create_producer()
            dlq_topic = f"dlq.{topic}"

            logger.info(f"Started consuming messages from topic: {topic}")

            for message in consumer:
                retries = 0
#                 producer.produce(dlq_topic, value=message.value)
#                 producer.flush()
                while retries < MAX_RETRIES:
                    try:
                        decoded_message = handle_message(message.value)
                        if not decoded_message or 'payload' not in decoded_message:
                            break

                        cursor = connection.cursor()
                        data = decoded_message['payload']
                        operation = data['op']

                        if handler_func(cursor, operation, data):
                            connection.commit()
                            consumer.commit()
                            cursor.close()
                            break
                        else:
                            connection.rollback()
                            consumer.commit()
                            cursor.close()
                            break
                    except Exception as e:
                        retries += 1
                        logger.error(f"Error processing message, retry {retries}/{MAX_RETRIES}: {e}")
                        time.sleep(RETRY_DELAY)
                        if 'cursor' in locals():
                            cursor.close()
                        connection.rollback()

                if retries >= MAX_RETRIES:
                    logger.warning(f"Max retries exceeded. Sending message to DLQ: {dlq_topic}")
                    producer.produce(dlq_topic, value=message.value)
                    producer.flush()

        except Exception as e:
            logger.error(f"Error in {topic} consumer: {e}")
            if 'connection' in locals() and connection:
                connection.close()
            time.sleep(5)

def process_dlq_messages(dlq_topic, handler_func):

    topic_name = dlq_topic.split('.')[1].split('-')[1] # "store_employee" 추출
    id_type = f"{topic_name}_id"  # "_id" 추가하여 id_type 생성
    logger.info(f"id_type : {id_type}")  # 이 줄 추가
    while True:
        logger.info(f"Checking DLQ topic: {dlq_topic}")
        try:
            connection = connect_to_database()
            if not connection:
                time.sleep(10)
                continue

            consumer = KafkaConsumer(
                dlq_topic,
                bootstrap_servers=['kafka-service:9092'],
                auto_offset_reset='earliest',
                enable_auto_commit=False,
                group_id=f"{dlq_topic.replace('.', '_')}_dlq_consumer"
            )

            for message in consumer:
                try:
                    decoded_message = handle_message(message.value)
                    if not decoded_message or 'payload' not in decoded_message:
                        continue

                    cursor = connection.cursor()
                    data = decoded_message['payload']
                    operation = data['op']

                    # DB에서 기존 version 가져오기
                    existing_version = get_existing_version_from_db(cursor, data['after'][id_type],id_type)

                    # 들어온 메시지의 version 값과 비교
                    incoming_version = data.get('after', {}).get('version')

                    if existing_version is not None and incoming_version is not None:
                        if incoming_version < existing_version:
                            # 들어온 메시지가 더 오래된 데이터라면 무시
                            logger.info(f"[DLQ 무시] 이전 처리된 메시지가 더 최신입니다. 무시: incoming_version={incoming_version}, existing_version={existing_version}")
                            consumer.commit()  # 커밋하여 다음 메시지로 넘어감
                            continue  # 현재 메시지 건너뛰기


                    if handler_func(cursor, operation, data):
                        connection.commit()
                        consumer.commit()
                        logger.info(f"[DLQ 처리 성공] {dlq_topic} 메시지 처리 완료: {data}")
                    else:
                        connection.rollback()
                        consumer.commit()
                        logger.warning(f"[DLQ 처리 실패] {dlq_topic} 메시지 처리 실패: {data}")

                    cursor.close()
                except Exception as e:
                    logger.error(f"Error retrying DLQ message: {e}")
                    if 'cursor' in locals():
                        cursor.close()
                    connection.rollback()

            time.sleep(60)  # 주기적으로 DLQ 확인 (60초 간격)

        except Exception as e:
            logger.error(f"Error in DLQ consumer for topic {dlq_topic}: {e}")
            if 'connection' in locals() and connection:
                connection.close()
            time.sleep(10)

def get_existing_version_from_db(cursor, id_value, id_type):
    try:
        # ID에서 "_id"를 제거하고 테이블명을 결정
        table_name = id_type.replace('_id', '')  # "_id" 제거하여 테이블명 결정
        logger.info(f"table_name : {table_name}")  # 이 줄 추가
        query = f"SELECT version FROM {table_name} WHERE {id_type} = %s"
        cursor.execute(query, (id_value,))

        result = cursor.fetchone()
        return result[0] if result else None
    except Exception as e:
        logger.error(f"Error fetching existing version from DB for {id_type}: {e}")
        return None
if __name__ == "__main__":
  main()
