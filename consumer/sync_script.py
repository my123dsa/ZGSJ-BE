from kafka import KafkaConsumer
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
          host="host.docker.internal",
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
                   (president_id, name, email, terms_accept) 
                   VALUES (%s, %s, %s, %s)"""
           values = (data['after']['president_id'],
                    data['after']['name'], 
                    data['after']['email'],
                    data['after']['terms_accept']) 
       elif operation == 'u': 
           sql = """UPDATE president 
                   SET name = %s,
                       email = %s,
                       terms_accept = %s
                   WHERE president_id = %s"""
           values = (data['after']['name'],
                    data['after']['email'],
                    data['after']['terms_accept'], 
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
                    location, latitude, longitude, president_id)  
                   VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"""

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
                     data['after']['president_id'])

        elif operation == 'u': 
            sql = """UPDATE store 
                   SET store_name = %s,
                       account_number = %s,
                       bank_code = %s,
                       location = %s,
                       latitude = %s, 
                       longitude = %s,
                       president_id = %s
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
                     bank_code, account_number, payment_date, birth_date, phone_number)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"""
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
                     data['after']['phone_number'])

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
                        phone_number = %s
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

def process_messages(topic, handler_func):
  while True:
      try:
          connection = connect_to_database()
          if not connection:
              time.sleep(5)
              continue

          consumer = KafkaConsumer(
              topic,
              bootstrap_servers=['kafka:29092'],
              auto_offset_reset='earliest',
              enable_auto_commit=True,
              group_id=f'{topic.replace(".", "_")}_sync_group',
              value_deserializer=None
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
                  else:
                      connection.rollback()
                  
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
           bootstrap_servers=['kafka:29092']
       )
       existing_topics = temp_consumer.topics()
       logger.info(f"Available topics: {existing_topics}")
       temp_consumer.close()
   except Exception as e:
       logger.error(f"Error checking topics: {e}")

   president_thread = threading.Thread(
       target=process_messages,
       args=(president_topic, handle_president_changes)
   )
   
   store_thread = threading.Thread(
       target=process_messages,
       args=(store_topic, handle_store_changes)
   )
   
   store_employee_thread = threading.Thread(
       target=process_messages,
       args=(store_employee_topic, handle_store_employee_changes)
   )
   
   president_thread.start()
   store_thread.start()
   store_employee_thread.start()
   
   try:
       president_thread.join()
       store_thread.join()
       store_employee_thread.join()
   except KeyboardInterrupt:
       logger.info("Shutting down...")

if __name__ == "__main__":
  main()
