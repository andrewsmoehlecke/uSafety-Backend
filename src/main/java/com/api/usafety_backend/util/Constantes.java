package com.api.usafety_backend.util;

public class Constantes {

    /*
     * Configurações de seguirança
     */
    public final long EXPIRACAO_TOKEN = 600000000;
    public final String SENHA_TOKEN = "9eb64b26-e6f0-4c3b-a164-893512b6ec15";
    public final String HEADER_AUTHORIZATION = "Authorization";
    public final String PREFIXO_TOKEN = "Bearer ";
    public final String CARGOS = "ADMIN,USUARIO";
    public final String CARGOS_KEY = "roles";

    /*
     * Validação com regex
     */
    public final String REGEX_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public final String REGEX_USERNAME = "^[a-zA-Z0-9_-]{3,15}$";
    public final String REGEX_IMAGEM_URL = "^(http(s?):)([/|.\\w\\s-])*\\.(?:jpg|gif|png)$";

    /*
     * Username padrão do administrador
     */
    public final String ADMIN_USERNAME = "ademir";

    /*
     * Cargos do sistema
     */
    public final String CARGO_ADMIN = "ADMIN";
    public final String CARGO_USUARIO = "USUARIO";

    /*
     * Mensagens de erro
     */
    public final String USUARIO_NAO_ENCONTRADO = "usuarioNaoEncontrado";
    public final String USUARIO_DESABILITADO = "usuarioDesabilitado";
    public final String USERNAME_SENHA_INCORRETA = "usernameOuSenhaIncorreta";

    /*
     * Tipos de tópicos
     */
    public final String TOPICO_DUVIDA = "DUVIDA";
    public final String TOPICO_DISCUSSAO = "DISCUSSAO";
    public final String TOPICO_CONTEUDO = "CONTEUDO";

    /*
     * Serviço de email
     */
    public final String ASSUNTO_RECUPERACAO_CONTA = "Recuperação de conta";

    /*
     * Email do sistema
     */
    public final String EMAIL = "andrewsmoehlcke@gmail.com";
    public final String SENHA_EMAIL = "1234567890acm";
}
