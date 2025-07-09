package model;

public class Gastos {
    public int id;
    public double casa, seguros, carro, educacao, internet, saude, cartao, contaAgua, contaLuz, outros;

    public double total() {
        return casa + seguros + carro + educacao + internet + saude + cartao + contaAgua + contaLuz + outros;
    }
}
