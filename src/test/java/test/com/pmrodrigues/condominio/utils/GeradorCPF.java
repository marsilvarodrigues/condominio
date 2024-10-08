package test.com.pmrodrigues.condominio.utils;

import java.util.Arrays;
import java.util.Random;

import static java.util.stream.Collectors.joining;

public class GeradorCPF {

    public static String gerarCPF() {
        Random random = new Random();
        int[] cpf = new int[11]; // Array para os 11 dígitos do CPF

        // Gera os primeiros 9 dígitos aleatórios
        for (int i = 0; i < 8; i++) {
            cpf[i] = random.nextInt(10);
        }

        // Calcula o primeiro dígito verificador
        cpf[9] = calcularDigito(cpf, 10);
        // Calcula o segundo dígito verificador
        cpf[10] = calcularDigito(cpf, 11);

        // Constrói o CPF no formato 000.000.000-00
        return Arrays.stream(cpf).mapToObj(Integer::toString).collect(joining());
    }

    private static int calcularDigito(int[] cpf, int peso) {
        int soma = 0;
        for (int i = 0; i < peso - 1; i++) {
            soma += cpf[i] * (peso - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

}

