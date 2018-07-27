d = {}

i = 0
lang = ['pt','en','es','fr']
total = [0 for k in range(len(lang))]

for ling in lang:
	filename = 'data/' + ling + '_trigram_count_filtered.tsv'
	soma = 0
	d[ling] = {}
	with open(filename,'r', encoding = 'utf-8') as file:
		for line in file.readlines():
			l = line.split('\t')
			soma = soma + int(l[1])
			d[ling][l[0]] = int(l[1])
	total[i] = soma
	i += 1

def langrec(frase):
	prob = [1 for k in range(len(lang))]
	for i in range(len(frase)-2):
		for j,ling in enumerate(lang):
			prob[j] = prob[j] * d[ling].get(frase[i] + frase[i+1] + frase[i+2] , 1) / total[j]
		max_prob = max(prob)
		for j in range(len(prob)):
			prob[j] /= max_prob
	res = lang[prob.index(max(prob))]
	print(prob)
	return res

print('Write your sentence:')
frase = ""
while frase.lower() != "exit":
	frase = input(' >> ')
	print(langrec(frase))