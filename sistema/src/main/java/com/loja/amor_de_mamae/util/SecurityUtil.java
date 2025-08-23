package com.loja.amor_de_mamae.util;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {
    
    private static final int BCRYPT_ROUNDS = 12;
    
    /**
     * Cria um hash BCrypt da senha
     */
    public static String hashPassword(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        return BCrypt.hashpw(senha, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    /**
     * Verifica se a senha plaintext corresponde ao hash
     */
    public static boolean checkPassword(String senha, String hash) {
        if (senha == null || hash == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(senha, hash);
        } catch (Exception e) {
            System.err.println("Erro ao verificar senha: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica se uma string é um hash BCrypt válido
     */
    public static boolean isPasswordHashed(String senha) {
        return senha != null && senha.startsWith("$2a$");
    }
    
    /**
     * Teste da criptografia
     */
    public static void main(String[] args) {
        String senhaTeste = "minhaSenha123";
        String hash = hashPassword(senhaTeste);
        
        System.out.println("=== Teste de Criptografia ===");
        System.out.println("Senha: " + senhaTeste);
        System.out.println("Hash: " + hash);
        System.out.println("Verificação correta: " + checkPassword(senhaTeste, hash));
        System.out.println("Verificação errada: " + checkPassword("senhaErrada", hash));
        System.out.println("É hash válido: " + isPasswordHashed(hash));
    }
}