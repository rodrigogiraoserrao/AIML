dicionario = {"ines": "feia", "rodrigo": "fixe", "henrique": "cromo"}

print(dicionario["ines"])

dicionario["mariana"] = 'pintora'

if "maria" in dicionario.keys():
	print(dicionario["maria"])
else:
	print("quem eh a maria?")

descricao = dicionario.get("bruno", "margem sul")
print(descricao)

print(dicionario.get("maria", "quem eh a maria?"))

while True:
	name = input(" >> ")
	print(dicionario.get(name, "quem eh o/a " + name + "?"))

d =	{"pt":
		{"aaa": 4, "asd": 34},
	{"en":
		{"the": 4352345}
	}
