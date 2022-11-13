- Faire des docstrings (celles de Donnees sont déjà faites)

- Gérer le fait que le déversement d'eau ne se fait pas instantanément (Logan is on it)

- Vérifier que le système temps/Evenement est opérationnel (pas d'abérration) (à faire après le point précédent)

- Implementer deux classes chef pompier avec :
1) Méthode pour algo élémentaire
-> Structure pour voir quel robot occupé (arrayList de Robot)
-> Structure avec quel incendie pris en charge par robot i (hashMap <Incendie, Robot>)
-> donneOrdre doit ajouter la liste d'évènements pour le robot
2) Méthode pour algo avancé
-> Méthode pour donner des ordres entre chef pompier et robots
-> Structure pour voir quel robot occupé avec quel incendie (hashmap?)
-> Méthode pour que un robot calcule son itinéraire le plus court
-> Méthode pour remplir son réservoir au point le plus proche
