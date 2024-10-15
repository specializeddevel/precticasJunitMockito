package org.rburgos.juni5app.examples.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.rburgos.juni5app.examples.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        System.out.println("Metodo de inicializacion");
        this.cuenta = new Cuenta("Raul", new BigDecimal("1000.12345"));
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test (clase)");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test (clase)");
    }

    @Test
    @DisplayName("Probando nombre de la cuenta")
    void testNombreCuenta() {
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
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

    }

    @Test
    @DisplayName("Las cuentas deben ser iguales")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("Jon Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Jon Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);

    }

    @Test
    @DisplayName("Saldo no nulo, saldo entero menos 100, saldo float menos 100")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Saldo correcto en int y float")
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Se lanza excepcion DineroInsuficienteException si no alcanza el saldo")
    void testDineroInsuficienteException() {
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
    //@Disabled
    @DisplayName("Probando relaciones entre las cuentas y el banco con assertAll")
    void testRelacionBancoCuentas() {
        //para hacer fallar un metodo se usa fail()
        //fail();
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

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows(){

    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void testSoloMac(){

    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testNoWindows(){

    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void soloJDK17() {

    }

    @Test
    @EnabledOnJre(JRE.JAVA_19)
    void soloJDK19() {

    }

    @Test
    @DisabledOnJre(JRE.JAVA_17)
    void testNoJDK17() {

    }

    @Test
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + "=" + v) );
    }


    @Test
    @EnabledIfSystemProperty(named = "java.specification.version", matches = "21")
    void testJavaVersion() {

    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    void testSoloArch32(){

    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "raulb")
    void testSoloUserName(){

    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testSoloEnvDes(){

    }

    @Test
    void imprimirVariablesAmbiente(){
        Map<String, String> getenv = System.getenv();
        getenv.forEach((k, v) -> System.out.println(k + "=" + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JD2_HOME", matches = ".*JDownloader 2.0*")
    void testJd2Home(){

    }
}