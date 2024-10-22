package org.rburgos.juni5app.examples.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.rburgos.juni5app.examples.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

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

    //Nested permite anidad metodos relacionados dentro de una clase (como una suite de casos de prueba)
    //Si un metodo de la clase Nested falla, entonces falla toda la clase
    @Nested
    @DisplayName("Probando atributos de la cuenta")
    class CuentaTestNombreSaldo {
        @Test
        @DisplayName("el nombre")
        void testNombreCuenta() {
            //cuenta.setPersona("Raul");
            String esperado = "Raul";
            String real = cuenta.getPersona();
            assertNotNull(real);
            assertEquals(esperado, real);
            assertTrue(real.equals("Raul"));
        }

        @Test
        @DisplayName("el saldo")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

        }

        @Test
        @DisplayName("iguales")
        void testReferenciaCuenta() {
            cuenta = new Cuenta("Jon Doe", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("Jon Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
            assertEquals(cuenta2, cuenta);

        }
    }

    @Nested
    class CuentaOperacionesTest {
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

    @Nested
    class SistemaOperativoTest {
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
    }

    @Nested
    class JavaVersionTest {
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
    }

    @Nested
    class SistemPropertiesTest {
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
    }

    @Test
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + "=" + v) );
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

    //assumeTrue
    //Se ejecuta si la variable de entorno ENV tiene el valor de dev
    @Test
    @DisplayName("Test saldo cuenta dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        //Si se cump[e assumeTrue se sigue con las instrucciones que estan despues
        assumeTrue(esDev);
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    //assumingThat
    @Test
    @DisplayName("Test saldo cuenta dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        //Si se cumple assumingThat se ejecuta la lambda, si no se cumple no se ejecuta pereo la prueba pasa
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    //Test repetitivo
    @DisplayName("Test saldo cuenta repetitivo")
    @RepeatedTest(value = 5, name = "{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepeated(RepetitionInfo repetitionInfo) {
        //Se ejecuta de acuerdo a los valores de la repeticion que llega en repetitionInfo
        if(repetitionInfo.getCurrentRepetition() == 3) {
            System.out.println("Estamos en la repeticion " + repetitionInfo.getCurrentRepetition());
        }
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Nested
    @DisplayName("Test parametrizados")
    class parameterizedTests{

        //VALUE
        //Test que toma valores de una fuente externa para el test
        @ParameterizedTest(name = "numero {index} ejecutando con {0} - {argumentsWithNames}")
        //Cuando se trabaja con Double se tiene menos precicion, es mejor trabajar con String
        //y Bigdecimal
        @ValueSource(strings = {"100","200","300","500","700", "1000.12345"})
        @DisplayName("Test Saldo positivo parametrizado")
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            System.out.println(cuenta.getSaldo());
        }

        //CSV
        @ParameterizedTest(name = "numero {index} ejecutando con {0} - {argumentsWithNames}")
        @CsvSource({"1,100","2,200","3,300","4,500","5,700", "6,1000.12344"})
        @DisplayName("Test Saldo positivo parametrizado CSV")
        void testDebitoCuentaCsvSource(String index, String monto) {
            System.out.println(index + " -> " + monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            System.out.println(cuenta.getSaldo());
        }

        //CSV 2
        @ParameterizedTest(name = "numero {index} ejecutando con {0} - {argumentsWithNames}")
        @CsvSource({"200,100,John,Andres","250,200,Pepe,Pepe","300,300,maria,Maria","510,500,Pablo,Pablo","750,700,Lucas,Luca", "1000.12344,1000.12344,Raul,Raul"})
        @DisplayName("Test Saldo positivo parametrizado CSV 2")
        void testDebitoCuentaCsvSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());

            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);

            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            System.out.println(cuenta.getSaldo());
        }

        //CSV File 1
        @ParameterizedTest(name = "numero {index} ejecutando con {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        @DisplayName("Test Saldo positivo parametrizado CsvFile")
        void testDebitoCuentaCsvFileSource(String monto) {
            System.out.println(monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            System.out.println(cuenta.getSaldo());
        }

        //CSV File 2
        @ParameterizedTest(name = "numero {index} ejecutando con {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        @DisplayName("Test Saldo positivo parametrizado CSV File 2")
        void testDebitoCuentaCsvFileSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());

            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);

            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            System.out.println(cuenta.getSaldo());
        }

        //METHOD
        @ParameterizedTest(name = "numero {index} ejecutando con {0} - {argumentsWithNames}")
        @MethodSource("montoList")
        @DisplayName("Test Saldo positivo parametrizado MethodSource")
        void testDebitoCuentaMethodSource(String monto) {
            System.out.println(monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            System.out.println(cuenta.getSaldo());
        }

        static List<String> montoList() {
            return Arrays.asList("100","200","300","500","700", "1000.12345");
        }
    }



}