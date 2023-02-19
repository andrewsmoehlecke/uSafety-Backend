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
    public final String SENHAS_NAO_COINCIDEM = "senhasNaoCoincidem";
    public final String SENHA_INCORRETA = "senhaIncorreta";
    public final String ERRO_ENVIAR_EMAIL = "erroAoEnviarEmail";
    public final String CODIGO_INVALIDO = "codigoInvalido";

    /*
     * Mensagens de sucesso
     */
    public final String USUARIO_CRIADO = "usuarioCriado";
    public final String USUARIO_ATUALIZADO = "usuarioAtualizado";
    public final String SENHA_ALTERADA = "senhaAlterada";
    public final String EMAIL_ENVIADO = "emailEnviado";
    public final String OK = "ok";

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
    public final String EMAIL = "usafety@gamers-ifsul.tk";
    public final String SENHA_EMAIL = "uSafety-email007";
}
