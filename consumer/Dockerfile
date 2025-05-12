FROM python:3.9-slim

WORKDIR /app

RUN apt-get update && \
    apt-get install -y gcc librdkafka-dev && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*


COPY sync_script.py .
COPY requirements.txt .

RUN pip install -r requirements.txt

CMD ["python", "sync_script.py"]