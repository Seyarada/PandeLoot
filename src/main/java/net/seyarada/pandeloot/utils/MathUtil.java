package net.seyarada.pandeloot.utils;

import net.seyarada.pandeloot.Config;
import org.bukkit.util.Vector;

import java.util.Random;

public class MathUtil {

    public static Vector calculateVelocity(Vector from, Vector to, double gravity, double heightGain) {
        // I have no idea who is the original author of this but thank you

        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(distanceSquared(from, to));
        double maxGain = Math.max(heightGain, (endGain + heightGain));

        double a = -horizDist * horizDist / (4 * maxGain);
        double b = horizDist;
        double c = -endGain;

        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
        double vy = Math.sqrt(maxGain * (gravity + 0.0013675090252708 * heightGain));
        double vh = vy / slope;

        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;

        double vx = vh * dirx;
        double vz = vh * dirz;

        return new Vector(vx, vy, vz);
    }

    private static double distanceSquared(Vector from, Vector to) {

        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();

        return dx * dx + dz * dz;
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public static Vector getVelocity(double expoffset, double expheight) {
        Random random = new Random();
        return new Vector(
                Math.cos(random.nextDouble() * Math.PI * 2.0D) * expoffset, expheight,
                Math.sin(random.nextDouble() * Math.PI * 2.0D) * expoffset);
    }

    public static String getDurationAsTime(long time) {
        int hours = (int) time / 3600;
        int temp = (int) time - hours * 3600;
        int mins = temp / 60;
        temp = temp - mins * 60;
        int secs = temp;
        if(hours>0) {
            String text = Config.getHoursText();
            text = text.replace("%h%", String.valueOf(hours));
            text = text.replace("%m%", String.valueOf(mins));
            text = text.replace("%s%", String.valueOf(secs));
            return text;
        }
        if(mins>0) {
            String text = Config.getMinutesText();
            text = text.replace("%m%", String.valueOf(mins));
            text = text.replace("%s%", String.valueOf(secs));
            return text;
        }
        String text = Config.getSecondsText();
        text = text.replace("%s%", String.valueOf(secs));
        return text;
    }

}
