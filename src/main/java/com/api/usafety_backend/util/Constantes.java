package com.api.usafety_backend.util;

public class Constantes {

    /*
     * Constantes para as configurações de seguirança
     */
    public static final int EXPIRACAO_TOKEN = 600000;
    public static final String SENHA_TOKEN = "9eb64b26-e6f0-4c3b-a164-893512b6ec15";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String PREFIXO_TOKEN = "Bearer ";
    public static final String CARGOS = "ADMIN,USUARIO";

    /*
     * Constantes para validação com regex
     */
    public static final String REGEX_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_-]{3,15}$";
    public static final String REGEX_IMAGEM_URL = "^(http(s?):)([/|.\\w\\s-])*\\.(?:jpg|gif|png)$";

    /*
     * Username padrão do administrador
     */
    public static final String ADMIN_USERNAME = "ademir";

    /*
     * Cargos do sistema
     */
    public static final String CARGO_ADMIN = "ADMIN";
    public static final String CARGO_USUARIO = "USUARIO";
}
