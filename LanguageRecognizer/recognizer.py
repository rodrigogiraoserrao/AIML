pt = open('pt_trigram_count_filtered.tsv','r+')
en = open('en_trigram_count_filtered.tsv','r+')
es = open('es_trigram_count_filtered.tsv','r+')
fr = open('fr_trigram_count_filtered.tsv','r+')

"""
with open('pt_trigram_count_filtered.tsv','r+') as pt:
	# aqui 'pt' refere-se ao ficheiro
	for line in pt.readlines():
		# cenas com a linha 'line'

# aqui ja esta fechado
"""

"""
linguagens = ["en", "es"]
for ling in linguagens:
	filename = ling + "_trigram_count_filtered.tsv"
	with open(filename, "r") as file:
		for line in file.readlines():
			# cenas

# feito
	
"""

total_pt = 0
#with open('pt_trigram_count_filtered.tsv','w') as pt:
for line in pt.readlines():
  l = line.split()
  total_pt = total_pt + int(l[1])
    
for line in pt.readlines():
  l = line.split()
  line.write(int(l[1])/total_pt)

total_en = 0
#with open('en_trigram_count_filtered.tsv','w') as en:
for line in en.readlines():
  l = line.split()
  total_en = total_en + int(l[1])
    
for line in en.readlines():
  l = line.split()
  line.write(int(l[1])/total_en)

total_es = 0
#with open('es_trigram_count_filtered.tsv','w') as es:
for line in es.readlines():
  l = line.split()
  total_es = total_es + int(l[1])
    
for line in es.readlines():
  l = line.split()
  line.write(int(l[1])/total_es)
    
total_fr = 0
#with open('fr_trigram_count_filtered.tsv','w') as fr:
for line in fr.readlines():
  l = line.split()
  total_fr = total_fr + int(l[1])
    
for line in fr.readlines():
  l = line.split()
  line.write(int(l[1])/total_fr)
    

def language_detect(s):
  prob = [0, 0, 0, 0]
  for i in [pt, en, es, fr]:
    for j in range(len(s)-3):
      for line in i.readlines():
        l = line.split()
        if l[1] == s[j] + s[j+1] + s[j+2]:
          prob[i] = int(l[3])
  
  for i in range(4):
    if max(prob) == prob[i]:
      result = str([pt, en, es, fr][i])
      
  return result

pt.close()
en.close()
fr.close()
es.close()

# para cada ficheiro
	# abres ficheiro
	# para cada linha
		# contas e somas quantos trigramas ha
		# guardas a linha num dicionario, na forma dicionario[trigrama] = valor
