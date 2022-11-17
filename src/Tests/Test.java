package Tests;

/**
 * Enum correspondant aux différents tests implémentés.
 * Celui-ci est utile afin délocaliser la classe {@code Simulation} dans un
 * fichier annexe et ainsi factoriser le code.
 * Utilisé seulement dans {@code Simulation.java}
 */
public enum Test {
    TestSimulation,
    TestScenarios,
    TestDijkstra,
    TestBasique,
    TestAvance
}