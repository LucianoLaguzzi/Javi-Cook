package util;


import model.AbstractEntity;

import javax.faces.context.FacesContext;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sebastian
 */
public abstract class Util {

    public static final String DATE_FORMAT_DDMMYYYY_BARRA = "dd/MM/yyyy";
    public static final String DATE_FORMAT_DDMMYYYY_GUION_MEDIO = "dd-MM-yyyy";
    public static final String NOT_FOUND = "{`error` : `not found` }".replace('`', '"');
    public static final String EXC_FINDBYID = "FIND_BY_ID";
    public static final String EXC_CREATE = "CREATE";
    public static final String EXC_UPDATE = "UPDATE";

    /**
     * Levanta una archivo de disco
     *
     * @param path     Path del archivo
     * @param filename Nombre del archivo
     * @return Texto del archivo
     */
    public static String getFile(String path, String filename) {
        FileReader inf;
        File inputFile;
        char[] text = new char[0];
        int length = 0;
        try {
            inputFile = new File(path + "/" + filename);
            length = (int) inputFile.length();
            inf = new FileReader(inputFile);
            char c[] = new char[length];
            inf.read(c);
            text = c;
            inf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = new String(text);
        return s;
    }

    /**
     * Formatea un Date de acuerdo al formato deseado y al Locale
     *
     * @param date   Fecha a formatear
     * @param format formato deseado
     * @param locale Locale
     * @return Fecha formateada
     */
    public static String format(Date date, String format, Locale locale) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un Date de acuerdo al formato deseado
     *
     * @param date   Fecha a formatear
     * @param format formato deseado
     * @return Fecha formateada
     */
    public static String format(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un Date de acuerdo al locale es AR
     *
     * @param date Fecha a formatear
     * @return Fecha formateada
     */
    public static String format(Date date) {
        try {
            Locale locale = new Locale("es", "AR");
            DateFormat sdf = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String formatFull() {
        try {
            Locale locale = new Locale("es", "AR");
            DateFormat sdf = DateFormat.getDateInstance(DateFormat.LONG, locale);
            return sdf.format(new Date());
        } catch (Exception ex) {
            return "";
        }
    }

    public static String format(Date date, int dateFormatStyle) {
        try {
            Locale locale = new Locale("es", "AR");
            DateFormat sdf = DateFormat.getDateInstance(dateFormatStyle, locale);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String formatFullTime(Date date) {
        try {
            Locale locale = new Locale("es", "AR");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
            return sdf.format(date);
        } catch (Exception ex) {
            System.out.println(ex);
            return "";
        }
    }

    /**
     * Formatea un Date de acuerdo al locale es AR
     *
     * @param sdate         String Fecha a formatear
     * @param formatInicial String Formato fecha actual
     * @param formatFinal   String Formato fecha final
     * @return Fecha formateada
     */
    public static String format(String sdate, String formatInicial, String formatFinal) {
        try {
            Date date = parseDate(sdate, formatInicial);
            return format(date, formatFinal);
        } catch (Exception ex) {
            System.out.println(ex);
            return "";
        }
    }

    /**
     * Formatea un Date de acuerdo al locale es AR
     *
     * @param sdate String Fecha a formatear
     * @return Fecha formateada
     */
    public static String format(String sdate) {
        try {
            DateFormat sdf2 = DateFormat.getDateInstance();
            Date date = sdf2.parse(sdate);
            Locale locale = new Locale("es", "AR");
            DateFormat sdf = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un Date de acuerdo al locale es AR
     *
     * @param sdate String Fecha a formatear
     * @return Fecha formateada
     */
    public static String formatLong(String sdate) {
        try {
            DateFormat sdf2 = DateFormat.getDateInstance();
            Date date = sdf2.parse(sdate);
            Locale locale = new Locale("es", "AR");
            DateFormat sdf = DateFormat.getDateInstance(DateFormat.LONG, locale);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Formatea un String a Date de acuerdo al formato deseado
     *
     * @param date   String a formatear
     * @param format formato deseado
     * @return Fecha
     */
    public static Date parseDate(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (ParseException ex) {
            return new Date();
        }
    }

    public static Date parseDateUS(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse(date);
        } catch (ParseException ex) {
            return new Date();
        }
    }

    /**
     * Castea de java.util.Date a java.sql.Date
     *
     * @param date Date
     * @return Date
     */
    public static java.sql.Date castToSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Date castToSqlDate(String date) {
        return castToSqlDate(Util.parseDate(date));
    }

    /**
     * Formatea un String a Date de acuerdo al formato estandar
     *
     * @param date String a formatear
     * @return Fecha
     */
    public static Date parseDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(date);
        } catch (ParseException ex) {
            return new Date();
        }
    }

    /**
     * Formatea un String a Date de acuerdo al formato estandar
     *
     * @param date String a formatear
     * @return Fecha
     */
    public static Date parseDateWithEx(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(date);
    }

    /**
     * Formatea un float de acuerdo al formato deseado
     *
     * @param f      float a formatear
     * @param format formato deseado
     * @return float formateado
     */
    public static String format(float f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }

    /**
     * Formatea un double de acuerdo al formato deseado
     *
     * @param d      double a formatear
     * @param format formato deseado
     * @return double formateado
     */
    public static String format(double d, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(d);
    }

    /**
     * Reemplaza el string oldPattern por el newPattern en el string s
     *
     * @param s          String origen
     * @param oldPattern String a reemplazar
     * @param newPattern Nuevo valor del string
     * @param ignoreCase Determina si considera mayusculas
     * @param replaceAll Determina si busca todas las ocurrencias
     * @return String reemplazado
     */
    public static String stringReplace(String s, String oldPattern,
                                       String newPattern, boolean ignoreCase,
                                       boolean replaceAll) {
        String resultado = s;
        String saux = s;
        if (ignoreCase) {
            saux = saux.toUpperCase();
            oldPattern = oldPattern.toUpperCase();
        }
        int inicio = saux.indexOf(oldPattern);
        int fin = s.length();
        int tamanio = oldPattern.length();
        if (inicio != -1) {
            //Existe el substring
            resultado = s.substring(0, inicio) + newPattern
                    + s.substring(inicio + tamanio, fin);
            if (replaceAll) {
                resultado = stringReplace(resultado, oldPattern, newPattern, ignoreCase,
                        replaceAll);
            }
        }
        return resultado;
    }

    /**
     * Reemplaza todas las ocurrencias de oldPattern por newPattern ignorando
     * mayusculas
     *
     * @param s          String origen
     * @param oldPattern String a reemplazar
     * @param newPattern Nuevo valor del string
     * @return String reemplazado
     */
    public static String stringReplace(String s, String oldPattern,
                                       String newPattern) {
        return stringReplace(s, oldPattern, newPattern, true, true);
    }

    /**
     * Retorna el nombre del mes actual
     *
     * @return Nombre del mes
     */
    public static String getNombreMes() {
        return getNombreMes(Integer.parseInt(format(new Date(), "MM")));
    }

    /**
     * Retorna el nombre del mes
     *
     * @param mes mes
     * @return nombre del mes
     */
    public static String getNombreMes(int mes) {
        String nombre = "";
        switch (mes) {
            case 1:
                nombre = "Enero";
                break;
            case 2:
                nombre = "Febrero";
                break;
            case 3:
                nombre = "Marzo";
                break;
            case 4:
                nombre = "Abril";
                break;
            case 5:
                nombre = "Mayo";
                break;
            case 6:
                nombre = "Junio";
                break;
            case 7:
                nombre = "Julio";
                break;
            case 8:
                nombre = "Agosto";
                break;
            case 9:
                nombre = "Septiembre";
                break;
            case 10:
                nombre = "Octubre";
                break;
            case 11:
                nombre = "Noviembre";
                break;
            case 12:
                nombre = "Diciembre";
                break;
        }
        return nombre;
    }
//
//    public static Dia getDayOfTheWeek(Date d) {
//        Dia nombre = null;
//        GregorianCalendar cal = new GregorianCalendar();
//        cal.setTime(horaCero(d));
//        int dia = cal.get(7);
//        switch (dia) {
//            case 1:
//                nombre = Dia.Domingo;
//                break;
//            case 2:
//                nombre = Dia.Lunes;
//                break;
//            case 3:
//                nombre = Dia.Martes;
//                break;
//            case 4:
//                nombre = Dia.Miercoles;
//                break;
//            case 5:
//                nombre = Dia.Jueves;
//                break;
//            case 6:
//                nombre = Dia.Viernes;
//                break;
//            case 7:
//                nombre = Dia.Sabado;
//        }
//
//        return nombre;
//    }

    public static void stop() {
        System.exit(-1);
    }

    public static int abs(int numero) {
        return numero > 0 ? numero : -numero;
    }

    public static float abs(float numero) {
        return numero > 0 ? numero : -numero;
    }

    /**
     * NVL si el valor es nulo, retorna el substituto
     *
     * @param value      Valor a chequear
     * @param substitute Valor substituto
     * @return El valor o el substituto si el primero es null
     */
    public static Object nvl(Object value, Object substitute) {
        return (value != null) ? value : substitute;
    }

    public static Long nvl(Long value, Long substitute) {
        return (value != null) ? value : substitute;
    }

    public static Long nvl(Long value) {
        return nvl(value, new Long(0L));
    }

    public static String nvl(String value, String substitute) {
        return (value != null) ? value : substitute;
    }

    public static String nvl(String value) {
        return nvl(value, "");
    }

    public static Date nvl(Date value, Date substitute) {
        return (value != null) ? value : substitute;
    }

    public static Date nvl(Date value) {
        return nvl(value, new Date());
    }

    public static int nvl(int value, int substitute) {
        return (value != 0) ? value : substitute;
    }

    public static double nvl(double value, double substitute) {
        return (value != 0) ? value : substitute;
    }

    public static float nvl(float value, float substitute) {
        return (value != 0) ? value : substitute;
    }

    public static long nvl(long value, long substitute) {
        return (value != 0) ? value : substitute;
    }

    // si el valor de value es "" le pone el subtituto
    public static String blank(String value, String substitute) {
        return (value.equals("")) ? substitute : value;
    }

    // si el valor de value es null o "" le pone el subtituto
    public static String nvlBlank(String value, String substitute) {
        String text = nvl(value);
        return blank(text, substitute);
    }

    public static String cript(String campo, String clave, int modo) {
        String res = "";
        char xClave, xCampo;
        int i, j, lcampo, lclave, xCrip;

        lcampo = campo.length();
        lclave = clave.length();
        i = 0;
        j = 0;
        while (j < lcampo) {
            xClave = clave.charAt(i);
            xCampo = campo.charAt(j);
            char r = 'r';

            xCrip = (int) xCampo + ((int) xClave * modo);
            while ((xCrip > 122) || (xCrip < 32)) {
                if (xCrip > 122) {
                    xCrip -= 91;
                } else {
                    xCrip += 91;
                }
            }
            res += (char) (xCrip);
            i++;
            j++;
            if (i >= lclave) {
                i = 0;
            }
        }
        return res;
    }

    public static String strZero(String st, int i) {
        int k;
        String aux;
        if (st.length() > i) {
            return st.substring(0, i - 1); //copy(st,1,i);
        } else {
            aux = "";
            k = i - st.length();
            if (st.substring(0, 1).equalsIgnoreCase("-")) { //copy(st,1,1)='-' then
                aux = "-";
                st = st.substring(1, st.length() - 2); // copy(st,2,length(st)-1);
                k = i - st.length() - 1;
            }
            for (int j = 1; j <= k; j++) {
                aux = aux + "0";
            }
            return aux + st;
        }
    }

    /**
     * Retorna true si la fechaUno es mayor que la fechaDos
     *
     * @param fechaUno
     * @param fechaDos
     * @return boolean
     */
    public static boolean fechaEsMayor(Date fechaUno, Date fechaDos) {
        return fechaUno.compareTo(fechaDos) > 0;
    }

    /**
     * Retorna true si la fechaUno es menor o igual que la fechaDos
     *
     * @param fechaUno
     * @param fechaDos
     * @return boolean
     */
    public static boolean fechaEsMenorOIgual(Date fechaUno, Date fechaDos) {
        return fechaUno.compareTo(fechaDos) <= 0;
    }

    /**
     * Retorna la fecha menor de las que se pasan por parámetro
     *
     * @param fechaUno
     * @param fechaDos
     * @return boolean
     */
    public static Date fechaMenor(Date fechaUno, Date fechaDos) {
        return fechaUno.compareTo(fechaDos) > 0 ? fechaDos : fechaUno;
    }

    /**
     * Retorna la fecha mayor de las que se pasan por parámetro
     *
     * @param fechaUno
     * @param fechaDos
     * @return boolean
     */
    public static Date fechaMayor(Date fechaUno, Date fechaDos) {
        return fechaUno.compareTo(fechaDos) > 0 ? fechaUno : fechaDos;
    }

    /**
     * Diferencia de meses entre fechas, si la cantidad de meses no es exacata
     * redonde para arriba Ej 1 mes y 10 dias, retorna dos meses.
     *
     * @param startDate
     * @param endDate
     * @return int
     */
    public static int mesesEntre(Date startDate, Date endDate) {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;

        if (monthsBetween < 0) {
            return 0;
        }

        return monthsBetween;
    }

    public static int mesesEntreOracle(Date startDate, Date endDate) {

        int meses = 0;

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        meses += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        meses += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;

        if (endDate.compareTo(startDate) > 0) {
            meses++;
        }

        if (meses < 0) {
            return 0;
        }

        return meses;
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Calcula la diferencia en años entre dos fechas
     *
     * @param a fecha 1
     * @param b fecha 2
     * @return diferencia de años
     */
    public static int calculateDifference(Date a, Date b) {
        int tempDifference = 0;
        int difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        if (a.compareTo(b) < 0) {
            earlier.setTime(a);
            later.setTime(b);
        } else {
            earlier.setTime(b);
            later.setTime(a);
        }

        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            tempDifference = 365
                    * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
            difference += tempDifference;
            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            tempDifference = later.get(Calendar.DAY_OF_YEAR)
                    - earlier.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;
            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        return difference;
    } //calculateDifference

    /**
     * retorna la edad dado una fecha
     *
     * @param d Fecha
     * @return Edad en años
     */
    public static int getEdad(Date d) {
        return getEdad(d, new Date());
    }

    public static int getEdad(Date d, Date h) {
        LocalDate desde = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hasta = h.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period p = Period.between(desde, hasta);
        return p.getYears();
    }

    public static String getEdadCompleta(Date d) {
        return getEdadCompleta(d, new Date());
    }

    public static String getEdadCompleta(Date d, Date h) {
        LocalDate desde = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hasta = h.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ;
        Period p = Period.between(desde, hasta);
        return "" + p.getYears() + "a" + p.getMonths() + "m";
    }

    /**
     * Retorna la fecha actual con hora 00:00:00
     *
     * @return Date
     */
    public static Date fechaHoy() {

        Date fechaHoy = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaHoy);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();

    }

    /**
     * Retorna la fecha y hora actual
     *
     * @return Date
     */
    public static Date fechaHoraHoy() {
        return new Date();
    }

    /**
     * Retorna la fecha actual
     *
     * @return String
     */
    public static String fechaActual() {
        return format(new Date());
    }

    /**
     * Retorna la hora actual
     *
     * @return String
     */
    public static String horaActual() {
        return format(new Date(), "HH:mm:ss");
    }

    /**
     * Retorna la fecha y hora actual
     *
     * @return String
     */
    public static String fechaHoraActual() {
        return format(new Date(), "dd/MM/yyyy HH:mm:ss");
    }

    /**
     * Retorna el anio de la fecha parámetro
     *
     * @param fecha
     * @return
     */
    public static int soloAnio(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        return calendar.get(Calendar.YEAR);
    }

    public static Date horaCero(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date ultimoSegundo(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }


    /**
     * Retorna anio actual
     *
     * @return int
     */
    public static int anioActual() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * Crea un directorio
     *
     * @param dirpath Path del directorio a crear
     */
    public static void mkdir(String dirpath) {
        File dir = new File(dirpath);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * Borra un archivo del servidor
     *
     * @param filepath Path del archivo a borrar
     * @return boolean
     */
    public static boolean delete(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * Verifica si existe un archivo del servidor
     *
     * @param filepath Path del directorio a crear
     * @return boolean
     */
    public static boolean exists(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    /**
     * método de clase
     *
     * @param d Date date que contiene la fecha a formatear
     * @return String con la fecha formateada
     */
    public static String dateFormat(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(d);
    }

    public static String dateFormat(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static String dateFormat(String date, String actualFormat, String newFormat) {
        System.out.println("Fecha que recibe: " + date);

        SimpleDateFormat sdfNew = new SimpleDateFormat(newFormat);
        SimpleDateFormat sdfOld = new SimpleDateFormat(actualFormat);

        Date d = new Date();
        try {
            d = sdfOld.parse(date);
            System.out.println("Fecha que parsea: " + d.toString());

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        String result = sdfNew.format(d);
        System.out.println("Fecha que devuelve: " + result);
        return result;
    }

    /**
     * método de clase
     *
     * @param n double contiene el número a formatear
     * @return String formateado a decimal
     */
    public static String decimalFormat(double n) {
        DecimalFormat df = new DecimalFormat("###,###0.0##");
        return df.format(n);
    }

    public static String decimalFormat(double n, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(n);
    }

    public static String floatFormat(double n, int decimals) {
        n = round(n, decimals);
        String num = Double.toString(n);
        int i = num.indexOf(".");
        if (i == -1) {
            num = num + ".00";
        } else {
            int l = num.length() - 1;
            int index = i + decimals;
            if (l != index) {
                num = appendRigthString(num, '0', (index - l));
            }
        }
        return num;
    } //floatFormat

    public static BigDecimal roundTwoDecimals(BigDecimal number) {
        return number.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Redondea un float a dos digitos decimales
     *
     * @param number
     * @return float
     */
    public static float roundTwoDecimals(float number) {
        // Para eliminar el error de presición antes de
        // redondear a dos decimales redondea a tres
        return (float) round((double) number, 2);
    }

    /**
     * Redondea un double en dos decimales
     *
     * @param valor
     * @return
     */
    public static double round(double valor) {
        return round(valor, 2);
    }

    /**
     * Redondea un double con n decimales
     *
     * @param valor
     * @param decimals decimales
     * @return
     */
    public static double round(double valor, double decimals) {
        double mult = Math.pow(10, decimals);
        valor = valor * mult;
        long r = Math.round(valor);
        return r / mult;
    }

    /**
     * método de clase
     *
     * @param n int contiene el número a formatear
     * @return String formateado a integer
     */
    public static String integerFormat(int n) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(n);
    }

    /**
     * método de clase
     *
     * @param n double contiene el valor a formatear
     * @return String formateado a currency
     */
    public static String currencyFormat(double n) {
        DecimalFormat df = new DecimalFormat("$###,###0.00#");
        return df.format(n);
    }

    public static String currencyFormat(double n, String format) {
        return decimalFormat(n, format);
    }

    /**
     * método de clase: rellena a derecha el string s con el caracter c hasta
     * una longituc length
     *
     * @param s      String a rellenar
     * @param c      char caracter de relleno
     * @param length int longitud final del string
     * @return String
     */
    public static String fillRigthString(String s, char c, int length) {
        int cant = length - s.length();
        for (int i = 0; i < cant; i++) {
            s += c;
        }
        return s;
    }

    /**
     * método de clase: rellena a izquierda el string s con el caracter c hasta
     * una longituc length
     *
     * @param s      String a rellenar
     * @param c      char caracter de relleno
     * @param length int longitud final del string
     * @return String
     */
    public static String fillLeftString(String s, char c, int length) {
        int cant = length - s.length();
        for (int i = 0; i < cant; i++) {
            s = c + s;
        }
        return s;
    }

    /**
     * método de clase: apendea a derecha cant caracteres al string s
     *
     * @param s    String a rellenar
     * @param c    char caracter de relleno
     * @param cant int cantidad de caracteres a apendear
     * @return String
     */
    public static String appendRigthString(String s, char c, int cant) {
        for (int i = 0; i < cant; i++) {
            s += c;
        }
        return s;
    }

    /**
     * método de clase: apendea a izquierda cant caracteres al string s
     *
     * @param s    String a rellenar
     * @param c    char caracter de relleno
     * @param cant int cantidad de caracteres a apendear
     * @return String
     */
    public static String appendLeftString(String s, char c, int cant) {
        for (int i = 0; i < cant; i++) {
            s = c + s;
        }
        return s;
    }

    /**
     * Levanta un archivo de disco
     *
     * @param path     Path del archivo (sin \)
     * @param filename Nombre del archivo
     * @return Retorna un vector de Strings con el contenido del archivo
     */
    public static Vector loadFromFile(String path, String filename) {
        return loadFromFile(path + "/" + filename);
    }

    /**
     * Levanta un archivo de disco
     *
     * @param filename Nombre del archivo incluyendo el path
     * @return Retorna un vector de Strings con el contenido del archivo
     */
    public static Vector loadFromFile(String filename) {
        String str = " ";
        Vector value = new Vector();
        RandomAccessFile archivo;
        try {
            archivo = new RandomAccessFile(filename, "r");
            do {
                str = archivo.readLine();
                if (str != null) {
                    value.add(str);
                }
            } while (str != null);
            archivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    } //loadFromFile

    /**
     * @param v        Vector vector de string que sera guardado
     * @param path     String path donde se guardara el archivo
     * @param fileName String nombre del archivo
     * @return boolean true si se guardo correctamente el archivo
     */
    public static boolean saveToFile(Vector v, String path, String fileName) {
        return saveToFile(v, path + "/" + fileName);
    } //saveToFile

    /**
     * @param v        Vector vector de string que sera guardado
     * @param fileName String nombre del archivo
     * @return boolean true si se guardo correctamente el archivo
     */
    public static boolean saveToFile(Vector v, String fileName) {
        boolean ok = false;
        try {
            //Open a file of the current name.
            File file = new File(fileName);
            //Create an output writer that will write to that file.
            //FileWriter handles international characters encoding conversions.
            FileWriter out = new FileWriter(file);
            Iterator i = v.iterator();
            while (i.hasNext()) {
                out.write((String) i.next());
            }
//      out.write(getNewPage());
            out.close();
            ok = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    } //saveToFile

//    public static String currencyToLetras(double monto) {
//        String p = Double.toString(monto);
//        PesosLetra pl = new PesosLetra();
//        return pl.getNumerosLetra(p);
//    } //currencyToLetras

    public static String hoy(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dias);
        return format(cal.getTime());
    }

    /**
     * Genera un vector [valor,..] a partir de un String "v,..."
     *
     * @param keys String
     * @return Vector
     */
    public static Vector toVector(String keys) {
        Vector v = new Vector();
        int index = keys.indexOf(",");
        int actual = 0;
        while (index != -1) {
            String valor = keys.substring(actual, index);
            actual = index + 1;
            v.add(valor);
            index = keys.indexOf(",", actual);
        }
        if (!keys.substring(actual).equals("")) {
            v.add(keys.substring(actual));
        }
        return v;
    }

    /**
     * Genera un int[] a partir de un String "v,..."
     *
     * @param keys String
     * @return int[]
     */
    public static int[] toIntArray(String keys) {
        Vector v = toVector(keys);
        return toIntArray(v);
    }

    /**
     * Genera un int[] a partir de una colección
     *
     * @param c Collection
     * @return int[]
     */
    public static int[] toIntArray(Collection c) {
        int[] result = new int[c.size()];
        Iterator it = c.iterator();
        int i = 0;
        while (it.hasNext()) {
            result[i++] = Integer.parseInt(it.next().toString());
        }
        return result;
    }

    /**
     * Genera un int[] a partir de un String "v,..."
     *
     * @param keys String
     * @return String[]
     */
    public static String[] toArray(String keys) {
        Vector v = toVector(keys);
        return toArray(v);
    }

    /**
     * Genera un int[] a partir de un String "v,..."
     *
     * @param v Vector
     * @return String[]
     */
    public static String[] toArray(Vector v) {
        String[] result = new String[v.size()];
        Enumeration e = v.elements();
        int i = 0;
        while (e.hasMoreElements()) {
            result[i++] = e.nextElement().toString();
        }
        return result;
    }

//    public static String numerosLetras(double valor) {
//        String numeros = Double.toString(valor);
//        PesosLetra pl = new PesosLetra();
//        return pl.getNumerosLetra(numeros);
//    }

    /**
     * Retorna Si o No de acuerdo al valor del boolean
     *
     * @param value boolean
     * @return String
     */
    public static String sino(boolean value) {
        return value ? "Si" : "No";
    }

    /**
     * Retorna S o N de acuerdo al valor del boolean
     *
     * @param value boolean
     * @return String
     */
    public static String sn(boolean value) {
        return sino(value).substring(0, 1);
    }

    /**
     * Formatea una colección de Items
     *
     * @param col        Collection
     * @param strinicial String
     * @param strdivisor String
     * @param strfinal   String
     * @return String
     */
    public static String toString(Collection col, String strinicial, String strdivisor, String strfinal) {
        if (col == null || col.isEmpty()) return "";
        String result = strinicial;
        Iterator i = col.iterator();
        while (i.hasNext()) {
            result += i.next().toString() + strdivisor;
        }
        result = result.substring(0, Math.min(result.length(), result.length() - strdivisor.length()));
        if (!result.isEmpty() && result.endsWith(strdivisor)) {
            result = result.substring(0, result.length() - 1);
        }
        return result + strfinal;
    }

    public static String getListHumanized(List objects, String separador, String metodo) {
        String s = "";
        if (metodo != null) {
            for (Object u : objects) {
                try {
                    s += u.getClass().getMethod(metodo).invoke(u) + separador;
                } catch (Exception e) {
                }
            }
        } else {
            for (Object u : objects) {
                s += u.toString() + separador;
            }
        }
        if (!s.isEmpty() && s.endsWith(separador)) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }


//    public static java.sql.Date restarFechasDias(java.sql.Date fch, int dias) {
//        Calendar cal = new GregorianCalendar();
//        cal.setTimeInMillis(fch.getTime());
//        cal.add(Calendar.DATE, -dias);
//        return new java.sql.Date(cal.getTimeInMillis());
//    }

    public static Date restarFechasDias(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, -dias);
        return new Date(cal.getTimeInMillis());
    }


    public static boolean betweenDate(Date fecha, Date desde, Date hasta) {
        boolean between = false;
        if (fecha.after(desde) && fecha.before(hasta)) {
            between = true;
        }
        return between;
    }

    public static java.sql.Date sumarFechasDias(java.sql.Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, +dias);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static Date sumarFechasDias(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, +dias);
        return cal.getTime();
    }

    public static java.sql.Date sumarHoras(Date fch, int horas) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.HOUR_OF_DAY, +horas);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static Date sumarMinutos(Date fch, int minutos) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.MINUTE, +minutos);
        return new Date(cal.getTimeInMillis());
    }

    public static java.sql.Date sumarDias(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DAY_OF_YEAR, +dias);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static java.sql.Date sumarMeses(Date fch, int meses) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.MONTH, +meses);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static java.sql.Date sumarAnios(Date fch, int anios) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.YEAR, +anios);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static int ultimoDiaDelMes() {
        Calendar cal = new GregorianCalendar();
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date getPrimerDiaDelAnio() {
        return getPrimerDiaDelAnio(new Date());
    }

    public static Date getPrimerDiaDelAnio(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha.getTime());
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }


    public static Date getUltimoDiaDelAnio() {
        return getUltimoDiaDelAnio(new Date());
    }

    public static Date getUltimoDiaDelAnio(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new java.sql.Date(cal.getTimeInMillis());
    }


    /**
     * Retorna ultimo día del més del més de la fecha que se recibe como
     * parámetro
     *
     * @param fecha
     * @return Date
     */
    public static Date ultimoDiaMes(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date primerDiaMes(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static java.sql.Date setDiaAlMesAnioActual(int dia) {
        // Pido la fecha de hoy al Calendar
        Calendar hoy = Calendar.getInstance();
        // Cambio el dia
        hoy.set(Calendar.DATE, dia);
        // Armo y retorno la fecha
        Date fecha = hoy.getTime();
        return new java.sql.Date(fecha.getTime());
    }

    public static String completeBarCode(long id) {
        String barCode = "";
        for (int i = String.valueOf(id).length(); i < 10; i++) {
            barCode += "0";
        }
        barCode += String.valueOf(id);
        return barCode;
    }

    public static List<String> getList(String lista, String separador) {
        List<String> s = new ArrayList();
        while (lista.contains(separador)) {
            String dato = lista.substring(0, lista.indexOf(separador));
            s.add(dato);
            lista = lista.substring(lista.indexOf(separador) + 1);
        }
        s.add(lista);
        return s;
    }

    /**
     * Genera un vector [valor,..] a partir de un String "v,..."
     *
     * @param keys String
     * @return Vector
     */
    public static List toList(String keys) {
        List v = new ArrayList();
        int index = keys.indexOf(",");
        int actual = 0;
        while (index != -1) {
            String valor = keys.substring(actual, index);
            actual = index + 1;
            v.add(valor);
            index = keys.indexOf(",", actual);
        }
        if (!keys.substring(actual).equals("")) {
            v.add(keys.substring(actual));
        }
        return v;
    }

//    public static String guardarArchivo(UploadedFile uploadedFile, String nombre, String path) throws IOException, Exception {
//        // write the inputStream to a FileOutputStream
//        File file = new File(path + nombre);
//        OutputStream out = new FileOutputStream(file);
//        InputStream is = uploadedFile.getInputstream();
//        int read = 0;
//        byte[] bytes = new byte[1024];
//        while ((read = is.read(bytes)) != -1) {
//            out.write(bytes, 0, read);
//        }
//        is.close();
//        out.flush();
//        out.close();
//        return nombre;
//    }

    /**
     * Envía un mail
     *
     * @param from     Para
     * @param to       De
     * @param subject  Asunto
     * @param bodyText Texto
     * @param smtpHost Host smtp
     */
//    public static void sendMail(String from, String to, String subject,
//                                String bodyText, String smtpHost) {
//        sendMail(from, to, subject, bodyText, smtpHost, "", "");
//    }

//    public static void sendMail(String from, String to, String subject,
//                                String bodyText, String smtpHost, String username, String password) {
//        sendMail(from, to, subject, bodyText, smtpHost, username, password, from, from);
//    }

    /**
     * Envía un mail
     *
     * @param from       Para
     * @param to         De
     * @param subject    Asunto
     * @param bodyText   Texto
     * @param smtpHost   Host smtp
     * @param username
     * @param password
     * @param replyTo
     * @param senderName
     */
//    public static void sendMail(String from, String to, String subject, String bodyText,
//                                String smtpHost, String username, String password, String replyTo, String senderName) {
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", username);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                s = Session.getDefaultInstance(p, null);
//            }
//            MimeMessage message = new MimeMessage(s);
//            InternetAddress ia = new InternetAddress(from, senderName);
//            message.setFrom(ia);
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject(subject);
//            message.setReplyTo(new javax.mail.Address[]{
//                    new javax.mail.internet.InternetAddress(replyTo, senderName)});
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html");
//            mp.addBodyPart(body);
//            message.setContent(mp);
//
//            Transport transport = s.getTransport("smtps");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        } catch (UnsupportedEncodingException uex) {
//
//        }
//    }
//
//    /**
//     * Envía un mail
//     *
//     * @param from     Para
//     * @param to       De
//     * @param subject  Asunto
//     * @param bodyText Texto
//     * @param smtpHost Host smtp
//     */
//    public static void sendMailOffice(String from, String to, String subject,
//                                      String bodyText, String smtpHost) {
//        sendMailOffice(from, to, subject, bodyText, smtpHost, "", "");
//    }
//
//    /**
//     * Envía un mail
//     *
//     * @param from     Para
//     * @param to       De
//     * @param subject  Asunto
//     * @param bodyText Texto
//     * @param smtpHost Host smtp
//     * @param username
//     * @param password
//     */
//    public static void sendMailOffice(String from, String to, String subject,
//                                      String bodyText, String smtpHost, String username, String password) {
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", username);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                p.put("mail.smtp.port", "587");
//                s = Session.getDefaultInstance(p, null);
//            }
//            MimeMessage message = new MimeMessage(s);
//            InternetAddress ia = new InternetAddress(from);
//            message.setFrom(ia);
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject(subject);
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html");
//            mp.addBodyPart(body);
//            message.setContent(mp);
//
//            Transport transport = s.getTransport("smtp");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, 587, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//    }

//    /**
//     * Envía un mail con adjuntos
//     *
//     * @param from     Para
//     * @param cuentas  De
//     * @param subject  Asunto
//     * @param adjunto
//     * @param bodyText Texto
//     * @param smtpHost Host smtp
//     * @param username
//     * @param password
//     */
//    public static boolean sendMail(String from, List<String> cuentas, String subject, String adjunto,
//                                   String bodyText, String smtpHost, String username, String password) {
//        boolean enviado = false;
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", username);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                s = Session.getDefaultInstance(p, null);
//            }
//            MimeMessage message = new MimeMessage(s);
//            InternetAddress ia = new InternetAddress(from);
//            message.setFrom(ia);
//            for (String cuenta : cuentas) {
//                message.addRecipient(Message.RecipientType.TO, new InternetAddress(cuenta));
//            }
//            message.setSubject(subject);
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html");
//            DataSource source = new FileDataSource(adjunto);
//            body.setDataHandler(new DataHandler(source));
//            body.setFileName(subject + ".pdf");
//            mp.addBodyPart(body);
//            message.setContent(mp);
//            Transport transport = s.getTransport("smtps");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//            enviado = true;
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//        return enviado;
//    }

    private Date date1;
    private Date date2;

//    public void onDateSelect(SelectEvent event) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
//    }
//
//    //    public void click() {
////        RequestContext requestContext = RequestContext.getCurrentInstance();
////
////        requestContext.update("form:display");
////        requestContext.execute("PF('dlg').show()");
////    }
    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public static boolean existsImage(Long id, String sufijo, AbstractEntity entity) {
        String imagen = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/uploads/" + entity.getClass().getSimpleName().toLowerCase() + "_" + id + "_" + sufijo);
        if (imagen + ".png" != null) {// || imagen + ".jpg" != null || imagen + ".jpeg" != null || imagen + ".gif" != null
            return true;
        } else {
            return false;
        }
    }

    public static void renameFile(String oldName, String newName) {
        File oldfile = new File(oldName);
        File newfile = new File(newName);

        if (oldfile.renameTo(newfile)) {
            System.out.println("Rename succesful");
        } else {
            System.out.println("Rename failed");
        }
    }

    /**
     * Encripta a SHA256
     *
     * @param password Password
     * @return
     */
    public static String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    /**
     * Retorna una instancia de la clase cuya ruta se pase como parámetro.
     *
     * @param clase
     * @return
     */
    public static Object instanceOf(String clase) {

        Class myClass = null;
        Constructor constructor = null;
        Object instanceOfClass = null;

        try {

            myClass = Class.forName(clase);
            constructor = myClass.getConstructor();
            instanceOfClass = constructor.newInstance();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }

        return instanceOfClass;
    }

    public static Method setMethod(Class clase, String metodo) {
        Method response = null;
        try {
            response = clase.getMethod(metodo);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    /**
     * Le aplica el porcentaje al monto que se pasa por párametro.
     *
     * @param monto
     * @param porcentajeAplicar
     * @return float
     */
    public static BigDecimal calcularImportePorcentaje(BigDecimal monto, BigDecimal porcentajeAplicar) {
        return (monto.multiply(porcentajeAplicar)).divide(new BigDecimal(100));
    }

    /**
     * Retorna un string conformado por el type de exception definido en
     * constantes en clase Util, y el nombre de la clase u otra referencia de
     * donde sucede la exception.
     *
     * @param type
     * @param message
     * @return
     */
    public static String getMessageException(String type, String message) {
        return type.toUpperCase() + "_" + message.toUpperCase() + "_EXCEPTION";
    }

    /**
     * Retorna true si el valueOne es igual a valueTwo.
     *
     * @param valueOne
     * @param valueTwo
     * @return
     */
    public static boolean equal(BigDecimal valueOne, BigDecimal valueTwo) {
        return valueOne.compareTo(valueTwo) == 0;
    }

    /**
     * Retorna true si el valueOne es mayor al valueTwo.
     *
     * @param valueOne
     * @param valueTwo
     * @return
     */
    public static boolean greaterThan(BigDecimal valueOne, BigDecimal valueTwo) {
        return valueOne.compareTo(valueTwo) == 1;
    }

    /**
     * Retorna true si el valueOne es menor al valueTwo.
     *
     * @param valueOne
     * @param valueTwo
     * @return
     */
    public static boolean lesserThan(BigDecimal valueOne, BigDecimal valueTwo) {
        return valueOne.compareTo(valueTwo) == -1;
    }

    public static String formatLONG(Date date) {
        try {
            Locale locale = new Locale("es", "AR");
            DateFormat sdf = DateFormat.getDateInstance(DateFormat.LONG, locale);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    public static void copyFile(String destino, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(destino));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Integer> yearsRange(int startYear, int endYear) {
        List<Integer> list = new ArrayList<Integer>();
        while (startYear <= endYear) {
            list.add(startYear++);
        }
        return list;
    }

    //-----------Office 365----------------------

//    /**
//     * Envía un mail
//     *
//     * @param from     Para
//     * @param to       De
//     * @param subject  Asunto
//     * @param bodyText Texto
//     * @param smtpHost Host smtp
//     * @param username
//     * @param password
//     */
//    public static void sendMailOffice365(String from, String to, String subject,
//                                         String bodyText, String smtpHost, String username, String password) {
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", username);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                p.put("mail.smtp.port", "587");
//                //p.put("mail.smtp.clave", password);
//                s = Session.getDefaultInstance(p, null);
//            }
//            MimeMessage message = new MimeMessage(s);
//            InternetAddress ia = new InternetAddress(from);
//            message.setFrom(ia);
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject(subject);
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html");
//            mp.addBodyPart(body);
//            message.setContent(mp);
//
//            Transport transport = s.getTransport("smtp");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, 587, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    /**
//     * Envía un mail con un adjunto
//     *
//     * @param from       Para
//     * @param to         De
//     * @param subject    Asunto
//     * @param bodyText   Texto
//     * @param attach     Adjunto
//     * @param attachName
//     * @param smtpHost   Host smtp
//     * @param senderName
//     * @param username
//     * @param password
//     */
//    public static void sendMailOffice365(String from, String to, String subject, String bodyText, String smtpHost, String username, String password, String senderName, String attach, String attachName) {
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", from);
//                p.put("mail.smtp.from", from);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                p.put("mail.smtp.port", "587");
//                s = Session.getDefaultInstance(p, null);
//            }
//            MimeMessage message = new MimeMessage(s);
//            InternetAddress ia = new InternetAddress(from, senderName);
//            message.setReplyTo(new javax.mail.Address[]{
//                    new javax.mail.internet.InternetAddress(from)
//            });
//            message.setFrom(ia);
//            message.setSender(ia);
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject(subject);
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html; charset=UTF-8");
//            MimeBodyPart body2 = new MimeBodyPart();
//            DataSource source = new FileDataSource(attach);
//            body2.setDataHandler(new DataHandler(source));
//            body2.setFileName(attachName);
//            mp.addBodyPart(body);
//            mp.addBodyPart(body2);
//            message.setContent(mp);
//
//            Transport transport = s.getTransport("smtp");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    /**
//     * Envía un mail con adjuntos
//     *
//     * @param from     Para
//     * @param cuentas
//     * @param subject  Asunto
//     * @param adjunto
//     * @param bodyText Texto
//     * @param smtpHost Host smtp
//     * @param username
//     * @param password
//     * @return
//     */
//    public static boolean sendMailOffice365(String from, List<String> cuentas, String subject, String adjunto,
//                                            String bodyText, String smtpHost, String username, String password) {
//        boolean enviado = false;
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", username);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                p.put("mail.smtp.port", "587");
//                s = Session.getDefaultInstance(p, null);
//            }
//            MimeMessage message = new MimeMessage(s);
//            InternetAddress ia = new InternetAddress(from);
//            message.setFrom(ia);
//            for (String cuenta : cuentas) {
//                message.addRecipient(Message.RecipientType.TO, new InternetAddress(cuenta));
//            }
//            message.setSubject(subject);
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html; charset=UTF-8");
//            DataSource source = new FileDataSource(adjunto);
//            body.setDataHandler(new DataHandler(source));
//            body.setFileName(subject + ".pdf");
//            mp.addBodyPart(body);
//            message.setContent(mp);
//            Transport transport = s.getTransport("smtp");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//            enviado = true;
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//        return enviado;
//    }
//
//    public static boolean sendMailOffice365Calendar(String from, String to, String subject, String bodyText,
//                                                    String smtpHost, String username, String password,
//                                                    String replyTo, String senderName, String iCalSubject, String iCalBody,
//                                                    Date startDate, Date endDate, String id, String toName,
//                                                    String location, String fromPhone, String attach, String attachName) {
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", username);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                p.put("mail.smtp.port", "587");
//                s = Session.getDefaultInstance(p, null);
//            }
//
//            // Define message
//            MimeMessage message = new MimeMessage(s);
//            message.addHeaderLine("text/calendar");
//            message.addHeaderLine("method=REQUEST");
//            message.addHeaderLine("charset=UTF-8");
//            message.addHeaderLine("component=VEVENT");
//
//            InternetAddress ia = new InternetAddress(from, senderName);
//            message.setFrom(ia);
//            message.setSender(ia);
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject(subject);
//            message.setReplyTo(new Address[]{new InternetAddress(replyTo, senderName)});
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart bodyCalendar = new MimeBodyPart();
//            bodyCalendar.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
//            bodyCalendar.setHeader("Content-ID", "calendar_message");
//            bodyCalendar.setDataHandler(new DataHandler(
//                    new ByteArrayDataSource(generateICalData(iCalSubject, iCalBody, startDate, endDate, id, toName, to, senderName, from, location, fromPhone), "text/calendar")));// very important
//            mp.addBodyPart(bodyCalendar);
//
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html; charset=UTF-8");
//            mp.addBodyPart(body);
//
//            MimeBodyPart bodyAttach = new MimeBodyPart();
//            DataSource source = new FileDataSource(attach);
//            bodyAttach.setDataHandler(new DataHandler(source));
//            bodyAttach.setFileName(attachName);
//            mp.addBodyPart(bodyAttach);
//
//            message.setContent(mp);
//
//            Transport transport = s.getTransport("smtp");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean sendMailOffice365Calendar(String from, String to, String subject, String bodyText,
//                                                    String smtpHost, String username, String password,
//                                                    String replyTo, String senderName, String iCalSubject, String iCalBody,
//                                                    Date startDate, Date endDate, String id, String toName,
//                                                    String location, String fromPhone) {
//        try {
//            Session s = Session.getInstance(System.getProperties(), null);
//            if (!"".equals(username)) {
//                Properties p = new Properties();
//                p.put("mail.host", smtpHost);
//                p.put("mail.user", username);
//                p.put("mail.smtp.auth", "true");
//                p.put("mail.smtp.starttls.enable", "true");
//                p.put("mail.smtp.port", "587");
//                s = Session.getDefaultInstance(p, null);
//            }
//
//            // Define message
//            MimeMessage message = new MimeMessage(s);
//            message.addHeaderLine("text/calendar");
//            message.addHeaderLine("method=REQUEST");
//            message.addHeaderLine("charset=UTF-8");
//            message.addHeaderLine("component=VEVENT");
//
//            InternetAddress ia = new InternetAddress(from, senderName);
//            message.setFrom(ia);
//            message.setSender(ia);
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject(subject);
//            message.setReplyTo(new Address[]{new InternetAddress(replyTo, senderName)});
//            MimeMultipart mp = new MimeMultipart();
//            MimeBodyPart bodyCalendar = new MimeBodyPart();
//            bodyCalendar.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
//            bodyCalendar.setHeader("Content-ID", "calendar_message");
//            bodyCalendar.setDataHandler(new DataHandler(
//                    new ByteArrayDataSource(generateICalData(iCalSubject, iCalBody, startDate, endDate, id, toName, to, senderName, from, location, fromPhone), "text/calendar")));// very important
//            mp.addBodyPart(bodyCalendar);
//
//            MimeBodyPart body = new MimeBodyPart();
//            body.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
//            body.setContent(bodyText, "text/html; charset=UTF-8");
//            mp.addBodyPart(body);
//
//            message.setContent(mp);
//
//            Transport transport = s.getTransport("smtp");
//            if (!transport.isConnected()) {
//                transport.connect(smtpHost, username, password);
//            }
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    //-----------Fin Office 365----------------------
//
//    public static String generateICalData(String iCalSubject, String bodyText, Date startDate, Date endDate, String id,
//                                          String toName, String to, String fromName, String from, String location, String fromPhone) {
//        ICalendar ical = new ICalendar();
//        ical.addProperty(new biweekly.property.Method(biweekly.property.Method.REQUEST));
//        ical.setProductId("-//MunicipalidadJunin//Turnos//ES-ES");
//        ical.setMethod("REQUEST");
//        TimezoneInfo timezoneInfo = new TimezoneInfo();
//        timezoneInfo.getTimezoneById("America/Argentina/Buenos_Aires");
//        ical.setTimezoneInfo(timezoneInfo);
//
//        //Creo el evento
//        VEvent event = new VEvent();
//
//        event.setSummary(iCalSubject); //Asunto
//        event.setDescription(bodyText); //Mensaje
//        event.setDateStart(startDate); //Fecha y hora del turno
//        event.setDateEnd(endDate); //Fecha y hora del turno
//
//        //Organizador
//        Organizer organizer = new Organizer(fromName, from);
//        event.setOrganizer(organizer);
//
//        //Contacto
//        if ("".equals(Util.nvl(fromPhone))) event.addContact(fromPhone);
//
//        //Location
//        if ("".equals(Util.nvl(location))) event.setLocation(location);
//
//        //Attendee
//        Attendee a = new Attendee(toName, to);
//        a.setParticipationLevel(ParticipationLevel.REQUIRED);
//        a.setParticipationStatus(ParticipationStatus.NEEDS_ACTION);
//        a.setCalendarUserType(CalendarUserType.INDIVIDUAL);
//        event.addAttendee(a);
//
//        event.setPriority(5); //Priority
//        event.setUid(id);  //Identificador unico para el evento
//        event.setSequence(1); //Secuencia
//        event.setStatus(Status.create(Status.CONFIRMED)); //Estado en confirmada
//        event.setCreated(new Date()); //Fecha de creacion
//        event.setLastModified(new Date()); //Ultima modificacion
//        event.setDateTimeStamp(new Date());
//
//        //Agrego el evento
//        ical.addEvent(event);
//
//        //Creo la alarma
//        Duration duration = Duration.parse("-PT1H");
//        Trigger trigger = new Trigger(duration, Related.START);
//        VAlarm alarm = VAlarm.display(trigger, "Text");
//        alarm.setAction(Action.display()); //Accion
//        alarm.setDescription(iCalSubject); //Descripcion
//
//        //Agrego la alarma
//        event.addAlarm(alarm);
//
//        TimeZone timeZone = TimeZone.getTimeZone("America/Argentina/Buenos_Aires");
//        return Biweekly.write(ical).tz(timeZone, true).go();
//    }

    //--------------------FECHAS----------------------------
    public static Date hoy() {
        return new Date();
    }

    /**
     * Retorna el numero del mes
     *
     * @param mes mes
     * @return numero del mes
     */
    public static int getNumeroMes(String mes) {
        int numero = 0;
        switch (mes) {
            case "Enero":
                numero = 1;
                break;
            case "Febrero":
                numero = 2;
                break;
            case "Marzo":
                numero = 3;
                break;
            case "Abril":
                numero = 4;
                break;
            case "Mayo":
                numero = 5;
                break;
            case "Junio":
                numero = 6;
                break;
            case "Julio":
                numero = 7;
                break;
            case "Agosto":
                numero = 8;
                break;
            case "Septiembre":
                numero = 9;
                break;
            case "Octubre":
                numero = 10;
                break;
            case "Noviembre":
                numero = 11;
                break;
            case "Diciembre":
                numero = 12;
                break;
        }
        return numero;
    }

    public static Date getPrimerDiaDelMes() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(new Date().getTime());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static Date getUltimoDiaDelMes() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(new Date().getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static long diferenciaDias(Date fecha1, Date fecha2) {
        long diff = fecha1.getTime() - fecha2.getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    public static long diferenciaMinutos(Date fecha1, Date fecha2) {
        long diff = fecha1.getTime() - fecha2.getTime();
        return diff / (60 * 1000);
    }

    public static Date getUltimoDiaDelMesHMS() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(new Date().getTime());
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getPrimerDiaDelMesHMS() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(new Date().getTime());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getPrimerDiaDelMesHMS(int year) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(new Date().getTime());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    //
    public static Date getUltimoDiaDelMesHMS(int year, int month) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static Date getPrimerDiaDelMesHMS(int year, int month) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static Date sumarSegundos(Date fch, int segundos) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.SECOND, +segundos);
        return new Date(cal.getTimeInMillis());
    }


    public static Date ceroHoras(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static Date getPrimerDiaDelMesAnterior() {
        Calendar cal = new GregorianCalendar();
        //cal.setTimeInMillis(new Date().getTime());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        return cal.getTime();
    }

    public static Date getUltimoDiaDelMesAnterior() {
        Calendar cal = new GregorianCalendar();
        //cal.setTimeInMillis(new Date().getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        return cal.getTime();
    }

    public static Date eliminarSegundos(Date fecha) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fecha.getTime());
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    //------------------------STRINGS--------------------------
    public static String partString(String texto, String separador, int part) {
        try {
            String[] parts = texto.split(separador);
            return parts[part];
        } catch (Exception ex) {
            return "";
        }
    }


    //--------------COLORES----------------
    public static void writeTo(InputStream in, OutputStream out) throws IOException {
        try {
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String getHex(int r, int g, int b) {
        return toHex(r, g, b);
    }

    /**
     * Devuelve un valor HEX compatible con el navegador web
     * que representa el color en el ColorModel sRGB predeterminado.
     *
     * @param r red
     * @param g green
     * @param b blue
     * @return a HEX value amigable con el navegador
     */
    public static String toHex(int r, int g, int b) {
        return toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
    }

    private static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    //-------------------------MAIL---------------------------------------------
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validarMail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    /**
     * Retorna el valor asociado a la clave en un bundle y si no existe retorna la clave
     *
     * @param key        Clave
     * @param bundlename Almacén
     * @param params     Valores de parámetros
     * @return Valor o la clave
     */
    public static String getValor(String key, String bundlename, String... params) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundlename);
        String result = key;
        try {
            result = MessageFormat.format(bundle.getString(key), params);
        } catch (Exception e) {
        }
        return result;
    }

    public static String removeHtmlTags(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\\<.*?>", "");
    }

    public static Date sumarHoraMinutos(Date fecha, int horas, int minutos) {
        //----------Sumo a la fecha-----------------
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MINUTE, minutos);
        calendar.add(Calendar.HOUR, horas);
        //----------Resultado Fecha Fin---------------
        Date fechaSum = calendar.getTime();
        return fechaSum;
    }

    public static String getRandomRgbColor(){
        Random rand = new Random();
        float r = (float) (rand.nextFloat() / 2f + 0.5);
        float g = (float) (rand.nextFloat() / 2f + 0.5);
        float b = (float) (rand.nextFloat() / 2f + 0.5);
        java.awt.Color randomColor = new java.awt.Color(r, g, b);
        return "rgb("+ randomColor.getRed() + ", " + randomColor.getGreen() + ", " + randomColor.getBlue() + ")";
    }

    public static String getValor(String key) {
        return getValor(key, "Bundle");
    }
}
