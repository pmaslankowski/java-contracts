#!/usr/bin/python3

def generate(n):
    for i in range(n):
        print(f'"old(x) != {i}",')
        print(f'"x != {i}",')
        print(f'"result != {i}",')

generate(100)
