package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.util.Map.entry;

public class Main {
    static final Map<String, Double> averageExpansesMap = Map.ofEntries(
            entry("Haus",0.30),
            entry("Möbel",0.04),
            entry("Essen",0.15),
            entry("Verkehr",0.14),
            entry("Telekom", 0.02),
            entry("Freizeit",0.11),
            entry("Kleidung",0.06),
            entry("Gesundheit",0.03),
            entry("Bildung",0.02),
            entry("Amazon", 0.09),
            entry("Sparen",0.04)
    );

    public static void main(String[] args) {
        System.out.println("sum of average: " + averageExpansesMap.values().stream().mapToDouble(Double::doubleValue).sum());

        var expansesMap = new HashMap<String, Double>();
        var sc = new Scanner(System.in);

        System.out.print("Geben Sie Ihr aktuelles Gesamteinkommen ein: ");
        var income = sc.nextDouble();

        averageExpansesMap.forEach((key, value) -> {
            System.out.print("Geben Sie die Kosten für '" + key + "' an: ");
            var cost = sc.nextDouble();
            expansesMap.put(key, cost);
        });

        computePercentage(income, expansesMap);

        System.out.print("Geben Sie Ihr gewünschtes Sparziel (in Prozent) ein: ");
        double desiredSavingsPercentage = sc.nextDouble();

        calculateSavingsSuggestions(income, expansesMap, desiredSavingsPercentage);
    }


    public static void computePercentage(double income, HashMap<String, Double> map) {
        map.forEach((key, value) -> {
            var percentage = Math.round((value / income) * 100.0) / 100.0;
            var differenceToOptimum = Math.round((averageExpansesMap.get(key) - percentage) * 100.0) / 100.0;
            var moneyLeftOrDebt = (differenceToOptimum < 0) ? "Du musst hier diesen Betrag reduzieren: " + (income * differenceToOptimum) + "€" : "Du hast diesen Betrag frei: " + (income * differenceToOptimum) + "€";
            System.out.println(key + ": " + percentage + "% | Differenz: " + differenceToOptimum + " " + moneyLeftOrDebt);
        });
    }

    public static void calculateSavingsSuggestions(double income, HashMap<String, Double> expensesMap, double desiredSavingsPerMonth) {
        double savingsDifference = desiredSavingsPerMonth - expensesMap.get("Sparen");

        if (savingsDifference <= 0) {
            System.out.println("Sie erreichen Ihr Sparziel bereits.");
            return;
        }

        double remainingIncome = income - (expensesMap.get("Haus") + expensesMap.get("Telekom") + expensesMap.get("Sparen"));

       var savingPercentage = Math.round((savingsDifference / remainingIncome) * 100.0) / 100.0;
       for(var expense : expensesMap.entrySet()) {
           if (!expense.getKey().equals("Haus") && !expense.getKey().equals("Telekom") && !expense.getKey().equals("Sparen")) {
               var newExpense = expense.getValue() - (expense.getValue() * savingPercentage);
               var suggestedSavings = expense.getValue() - newExpense;
               System.out.printf("%s: Aktuell: %.2f€, Vorschlag: %.2f€ (Einsparung: %.2f€)\n",
                       expense.getKey(), expense.getValue(), newExpense, suggestedSavings);
           }
       }
    }

}