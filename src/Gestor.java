import java.io.*;
import java.util.*;

public class Gestor {

    private static LinkedHashMap<String, Alumne> Alumnes = new LinkedHashMap<>();
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main (String[] args) throws IOException {

        String Option = "";
        boolean Correct = true;
        printHelp();

        while (!Option.equals("x")) {

            if (Correct) {
                Correct = true;
                Option = demanaOpcio();
            }
            String[] options = Option.split(" ");
            switch (options[0]) {
                case "a":
                    addAlumne(options);
                    break;
                case "m":
                    System.out.println(printAlumnes(false));
                    break;
                case "t":
                    approveAll();
                    System.out.println("Alumnes aprovats.");
                    break;
                case "n":
                    changeName(options);
                    break;
                case "c":
                    changeNote(options);
                    break;
                case "d":
                    desa(options);
                    break;
                case "r":
                    recupera(options);
                    break;
                case "?":
                    printHelp();
                    break;
                case "x":
                    System.out.println("Fins a una altra!");
                    break;
                default:
                    System.out.println("Opció incorrecta.");
                    Option = demanaOpcio();
                    Correct = false;

            }
        }

        Alumnes.put("Juan Carlos", new Alumne(2, 2, 3));

    }

    private static void recupera(String[] options) throws IOException {
        if (options.length == 2) {

            BufferedReader buffReader = null;
            String line = "";
            try{
                File file = new File (options[1]);
                FileReader fileReader = new FileReader (file);
                buffReader = new BufferedReader(fileReader);

            } catch (FileNotFoundException e){
                System.out.println("Ruta d'arxiu incorrecte.");
            }

            while ((line = buffReader.readLine()) != null) {
                LinkedList<String> splittedLine = new LinkedList<String>(Arrays.asList(line.split("\t\t")));
                splittedLine.remove(splittedLine.size() - 1);
                splittedLine.add(0, "a");
                splittedLine.set(2, splittedLine.get(2).substring(splittedLine.get(2).length() - 1));
                String[] array = new String[splittedLine.size()];
                array = splittedLine.toArray(array);
                addAlumne(array);
            }
            buffReader.close();
        } else {
            System.out.println("'r' té un argument: (r 'ruta arxiu a llegir')");
        }
    }

    private static void desa(String[] options) {
        if (options.length == 2) {
            if (goodName(options)) {
                try {
                    FileWriter file = new FileWriter(options[1]);
                    PrintWriter writer = new PrintWriter(file);
                    writer.println(printAlumnes(true));
                    file.close();

                } catch (Exception e) {
                    System.out.println("Error a l'escriure l'arxiu.");
                }
            } else {
                System.out.println("Nom incorrecte.");
            }
        }else {
            System.out.println("'d' té un argument: (d 'ruta arxiu a escriure')");
        }
    }

    private static boolean goodName(String[] options) {
        CharSequence[] cs = new CharSequence[]{">", "<", "|", ":", "&"};

        for(CharSequence ch : cs) {
            if (options[1].contains(ch)){
                return false;
            }
        }
        return true;
    }

    private static void printHelp() {
        System.out.println("Ajuda\n" +
                "---------------------------------------------\n" +
                "a - Afegir nota\n" +
                "\tSintaxi: a <nom> <nota1> <nota2> <nota3>\n" +
                "m - Mostra els alumnes i les seves notes\n" +
                "t - Aprova a tothom\n" +
                "n - Canvia de nom\n" +
                "\tSintaxi: n <nom> <nom nou>\n" +
                "c - Canvia una nota\n" +
                "\tSintaxi: c <nom> <evaluació> <nota nova>\n" +
                "d - Desa l'estat a un fitxer\n" +
                "\tSintaxi: d <ruta fitxer>\n" +
                "r - Recupera l'estat d'un fitxer\n" +
                "\tSintaxi: r <ruta fitxer>\n" +
                "? - Mostra l'ajuda\n" +
                "x - Finalitza l'execució\n" +
                "---------------------------------------------");
    }

    private static void changeNote(String[] options) {
        if (options.length == 4) {
            Iterator<Map.Entry<String, Alumne>> AlumnesIterator = Alumnes.entrySet().iterator();
            boolean Trobat = false;
            while (AlumnesIterator.hasNext()) {
                Map.Entry<String, Alumne> entry = AlumnesIterator.next();
                if (entry.getKey().equals(options[1])) {
                    Trobat = true;
                    if (isNote(options[3])) {
                        switch (options[2]) {
                            case "1":
                                entry.getValue().setNota1(Integer.parseInt(options[3]));
                                System.out.println("Nota canviada.");
                                break;
                            case "2":
                                entry.getValue().setNota2(Integer.parseInt(options[3]));
                                System.out.println("Nota canviada.");
                                break;
                            case "3":
                                entry.getValue().setNota3(Integer.parseInt(options[3]));
                                System.out.println("Nota canviada.");
                            default:
                                System.out.println("Les evaluacions son 1, 2 o 3.");
                        }
                    } else {
                        System.out.println("Les notes son nombres del 0 al 10.");
                    }
                }
            }
            if (!Trobat) {
                System.out.println("Alumne no trobat.");
            }
        } else {
            System.out.println("'c' té 3 arguments:\n(c 'nom' 'evaluació' 'nota nova')");
        }
    }

    private static boolean isNote(String option) {
        try {
            if (Integer.parseInt(option) >= 0 && Integer.parseInt(option) <= 10) {
                return true;
            }
        }catch (Exception e) {
        }
        return false;
    }

    private static void changeName(String[] options) {
        if (options.length == 3) {
            Iterator<Map.Entry<String, Alumne>> AlumnesIterator = Alumnes.entrySet().iterator();
            boolean Trobat = false;
            while (AlumnesIterator.hasNext()) {
                try {
                    Map.Entry<String, Alumne> entry = AlumnesIterator.next();
                    if (entry.getKey().equals(options[1])) {
                        Trobat = true;
                        String[] newOptions = new String[]{"a", options[2],
                                String.valueOf(entry.getValue().getNota1()),
                                String.valueOf(entry.getValue().getNota2()),
                                String.valueOf(entry.getValue().getNota3())};

                        if (addAlumne(newOptions)) {
                            Alumnes.remove(entry.getKey());
                            System.out.println("Nom canviat.");
                        }
                    }
                } catch (ConcurrentModificationException e) {
                    break;
                }
            }
            if (!Trobat) {
                System.out.println("Alumne no trobat.");
            }
        }else {
            System.out.println("'n' té 2 arguments:\n(n 'nom' 'nom nou')");
        }
    }

    private static void approveAll() {
        Iterator<Map.Entry<String, Alumne>> AlumnesIterator = Alumnes.entrySet().iterator();

        while (AlumnesIterator.hasNext()) {
            Map.Entry<String, Alumne> entry = AlumnesIterator.next();
            if (calculaNotaFinal(entry.getValue()) < 5) {
                if (entry.getValue().getNota1() < 5) {
                    entry.getValue().setNota1(5);
                }
                if (entry.getValue().getNota2() < 5) {
                    entry.getValue().setNota2(5);
                }
                if (entry.getValue().getNota3() < 5) {
                    entry.getValue().setNota3(5);
                }
            }
        }
    }

    private static boolean addAlumne(String[] options) {

        if (options.length == 5) {
            try {
                if (!Alumnes.containsKey(options[1])) {
                    if (areNotes(options)) {
                        Alumnes.put(options[1],
                                new Alumne(Integer.parseInt(options[2]),
                                        Integer.parseInt(options[3]),
                                        Integer.parseInt(options[4])));
                        System.out.println("Alumne afegit.");
                    }else {
                        System.out.println("Les notes son nombres del 0 al 10.");
                    }

                }else {
                    System.out.println("Alumne ja introduït.\nPer modificar la nota usa la comanda 'c'");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error en l'introducció dels arguments.");
            }
        }else {
            System.out.println("'a' té 4 arguments:\n(a 'nom' 'nota1' 'nota2' 'nota3')");
        }
        return true;
    }

    private static boolean areNotes(String[] options) {
        if ((Integer.parseInt(options[2]) >= 0 && Integer.parseInt(options[2]) <= 10)
                &&(Integer.parseInt(options[3]) >= 0 && Integer.parseInt(options[3]) <= 10)
                &&(Integer.parseInt(options[4]) >= 0 && Integer.parseInt(options[4]) <= 10)) {
            return true;
        }
        return false;
    }

    private static String demanaOpcio() throws IOException {
        String Option = "";
        System.out.print("Intodueix la opció: ");
        Option = reader.readLine();
        return Option;
    }

    public static String printAlumnes(boolean isFile) {
        Iterator<Map.Entry<String, Alumne>> AlumnesIterator = Alumnes.entrySet().iterator();

        if (isFile) {
            System.out.println("Arxiu creat.");
        }else {
            System.out.println("alumnes\t\t\t1ra\t\t2na\t\t3ra\t\tfinal");
            System.out.println("---------------------------------------------");
        }

        String Output = "";

        while (AlumnesIterator.hasNext()) {
            Map.Entry<String, Alumne> entry = AlumnesIterator.next();
            Output += (entry.getKey() + "\t\t\t" + entry.getValue().getNota1()
                                + "\t\t" + entry.getValue().getNota2()
                                + "\t\t" + entry.getValue().getNota3()
                                + "\t\t" + calculaNotaFinal(entry.getValue()) + "\n");
        }

        return Output.substring(0, Output.length() - 1);
    }

    private static double calculaNotaFinal(Alumne alumne) {

        return round(((double)(alumne.getNota1() + alumne.getNota2() + alumne.getNota3()) / 3), 1);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
