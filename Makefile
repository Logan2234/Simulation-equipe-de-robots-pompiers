# Ensimag 2A POO - TP 2018/19
# ============================
#
# Ce Makefile permet de compiler le test de l'ihm en ligne de commande.
# Alternative (recommandee?): utiliser un IDE (eclipse, netbeans, ...)
# Le but est ici d'illustrer les notions de "classpath", a vous de l'adapter
# a votre projet.
#
# Organisation:
#  1) Les sources (*.java) se trouvent dans le repertoire src
#     Les classes d'un package toto sont dans src/toto
#     Les classes du package par defaut sont dans src
#
#  2) Les bytecodes (*.class) se trouvent dans le repertoire bin
#     La hierarchie des sources (par package) est conservee.
#     L'archive bin/gui.jar contient les classes de l'interface graphique
#
# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

all: testScenarios testSimulation

testInvader:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Tests/TestInvader.java

testLecture:
	javac -d bin -sourcepath src src/Tests/TestLecteurDonnees.java

# Nos tests sont en-dessous de ce commentaire
testSimulation:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Tests/TestSimulation.java

testScenarios:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Tests/TestScenarios.java

testDijkstra:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/Tests/TestDijkstra.java

# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader
exeInvader: 
	java -classpath bin:bin/gui.jar TestInvader

exeLecture: 
	java -classpath bin TestLecteurDonnees cartes/spiralOfMadness-50x50.map

# Nos tests sont en dessous de ce commentaire
exeSimulation: 
	java -classpath bin:bin/gui.jar Tests/TestSimulation cartes/carteSujet.map

exeScenarios:
	java -classpath bin:bin/gui.jar Tests/TestScenarios cartes/carteSujet.map

exeDijkstra:
	java -classpath bin:bin/gui.jar Tests/TestDijkstra cartes/carteSujet.map

clean:
	rm -rf bin/Donnees bin/Evenements bin/Tests bin/io bin/Chemin
