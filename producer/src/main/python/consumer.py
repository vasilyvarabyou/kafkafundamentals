import requests

res = requests.get(url="http://localhost:8081/transactions")

print(len(res.json()))
