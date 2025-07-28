import requests
import random

INDEX_URL = "http://localhost:9200/userpreview/_doc"
AVATAR_BASE = "https://cdn.metaclone.com/avatars"

users = [
    {"login": "elonmusk", "first_name": "Elon", "last_name": "Musk"},
    {"login": "e.musk", "first_name": "Elon", "last_name": "Must"},
    {"login": "grimeslover", "first_name": "Claire", "last_name": "Boucher"},
    {"login": "taylor.swift", "first_name": "Taylor", "last_name": "Swift"},
    {"login": "tswift13", "first_name": "Taylor", "last_name": "Swift"},
    {"login": "drake", "first_name": "Aubrey", "last_name": "Graham"},
    {"login": "aubgraham", "first_name": "Aubrey", "last_name": "Graham"},
    {"login": "beyonce", "first_name": "Beyonce", "last_name": "Knowles"},
    {"login": "bknowles", "first_name": "Bey", "last_name": "Knowles"},
    {"login": "zuckerberg", "first_name": "Mark", "last_name": "Zuckerberg"},
    {"login": "m.zuck", "first_name": "Mark", "last_name": "Zukerberg"},
    {"login": "billg", "first_name": "Bill", "last_name": "Gates"},
    {"login": "gatesb", "first_name": "William", "last_name": "Gates"},
    {"login": "ironman", "first_name": "Tony", "last_name": "Stark"},
    {"login": "tstark", "first_name": "Anthony", "last_name": "Stark"},
    {"login": "blackwidow", "first_name": "Natasha", "last_name": "Romanoff"},
    {"login": "romanova", "first_name": "Natasha", "last_name": "Romanova"},
    {"login": "spiderman", "first_name": "Peter", "last_name": "Parker"},
    {"login": "pparker", "first_name": "Peter", "last_name": "Parker"},
    {"login": "batman", "first_name": "Bruce", "last_name": "Wayne"},
]

for i, user in enumerate(users, start=1):
    payload = {
        "userId": i,
        "login": user["login"],
        "first_name": user["first_name"],
        "last_name": user["last_name"],
        "avatarUrl": f"{AVATAR_BASE}/{i}.jpg"
    }

    response = requests.post(f"{INDEX_URL}/{i}", json=payload)
    if response.status_code not in (200, 201):
        print(f"Failed to insert user {i}: {response.status_code} {response.text}")
    else:
        print(f"Inserted user {i}: {user['first_name']} {user['last_name']} ({user['login']})")
