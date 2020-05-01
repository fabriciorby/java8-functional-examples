package example.pojo;

public class Usuario {

    private String nome;
    private Integer idade;
    private String sobrenome;
    private Integer id;
    private String cpf;

    public Usuario() { }

    public Usuario(String nome, String sobrenome,  String cpf, Integer idade, Integer id) {
        this.nome = nome;
        this.idade = idade;
        this.sobrenome = sobrenome;
        this.id = id;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", sobrenome='" + sobrenome + '\'' +
                ", id=" + id +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
