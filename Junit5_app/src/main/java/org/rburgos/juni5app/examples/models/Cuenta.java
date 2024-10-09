package org.rburgos.juni5app.examples.models;

import org.rburgos.juni5app.examples.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

public class Cuenta {
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String nombre, BigDecimal saldo) {
        this.saldo = saldo;
        this.persona = nombre;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) {
        BigDecimal nuevoSaldo = saldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new DineroInsuficienteException("Dinero Insuficiente");
        }
        this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto){
        this.saldo = saldo.add(monto);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Cuenta)) {
            return false;
        }
        if (this.persona == null || this.saldo == null){
            return false;
        }
        Cuenta c = (Cuenta) obj;
        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
}
