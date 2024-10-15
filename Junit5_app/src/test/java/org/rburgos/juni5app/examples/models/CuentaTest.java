package org.rburgos.juni5app.examples.models;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rburgos.juni5app.examples.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    @DisplayName("Probando nombre de la cuenta")
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Raul", new BigDecimal("1000.12345"));
        //cuenta.setPersona("Raul");
        String esperado = "Raul";
        String real = cuenta.getPersona();
        assertNotNull(real);
        assertEquals(esperado, real);
        assertTrue(real.equals("Raul"));
    }

    @Test
    @DisplayName("Que la cuenta tenga valor correcto, el saldo sea mayor a 0")
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Raul", new BigDecimal("1000.12345"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

    }

    @Test
    @DisplayName("Las cuentas deben ser iguales")
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("Jon Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Jon Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);

    }

    @Test
    @DisplayName("Saldo no nulo, saldo entero menos 100, saldo float menos 100")
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Saldo correcto en int y float")
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Se lanza excepcion DineroInsuficienteException si no alcanza el saldo")
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String expected = "Dinero Insuficiente";
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Se transfiere de cuenta1 a cuenta2")
    void testTransferirDineroCuentas() {
        Cuenta cuentaOrigen = new Cuenta("Andres", new BigDecimal("2500"));
        Cuenta cuentaDestino = new Cuenta("Raul", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.setNombre("Banco de la nacion");
        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500));
        assertEquals("2000", cuentaOrigen.getSaldo().toPlainString());
        assertEquals("2000.8989", cuentaDestino.getSaldo().toPlainString());


    }

    @Test
    @Disabled
    @DisplayName("Probando relaciones entre las cuentas y el banco con assertAll")
    void testRelacionBancoCuentas() {
        //para hacer fallar un metodo se usa fail()
        fail();
        Cuenta cuentaOrigen = new Cuenta("Andres", new BigDecimal("2500"));
        Cuenta cuentaDestino = new Cuenta("Raul", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuentaOrigen);
        banco.addCuenta(cuentaDestino);
        banco.setNombre("Banco de la nacion");

        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500));
        assertAll(() -> {
                    assertEquals("2000", cuentaOrigen.getSaldo().toPlainString(),
                            () -> "El saldo no es correcto, se esperaba 2000" +
                            " pero se recibio " + cuentaOrigen.getSaldo().toPlainString());
                },
                () -> {
                    assertEquals("2000.8989", cuentaDestino.getSaldo().toPlainString(), () -> "El saldo no es correcto");
                },
                () -> {
                    assertEquals(2, banco.getCuentas().size());
                },
                () -> {
                    assertEquals("Banco de la nacion", cuentaOrigen.getBanco().getNombre());
                },
                () -> {
                    assertEquals("Raul", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Raul"))
                            .findFirst()
                            .get()
                            .getPersona()
                    );
                },
                () -> {
                    assertTrue(banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Raul"))
                            .findAny()
                            .isPresent()
                    );
                },
                () -> {
                    assertTrue(banco.getCuentas().stream()
                            .anyMatch(e -> e.getPersona().equals("Andres")));
                });


    }

}