import requests
import random

def build_random_transactions(clientId):
    return {
        "bank": "Bank",
        "clientId": clientId,
        "orderType": "INCOME",
        "quantity": 1,
        "price": 1.00
    }

def post_transaction(trx):
    resp = requests.post(url="http://localhost:8080/transactions", json=trx)
    if not resp.ok:
        raise Exception("POST not OK: " + resp.reason)

for x in range(0, 1000):
    post_transaction(build_random_transactions(x))