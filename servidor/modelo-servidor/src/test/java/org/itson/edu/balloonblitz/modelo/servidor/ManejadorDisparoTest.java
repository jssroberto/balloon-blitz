package org.itson.edu.balloonblitz.modelo.servidor;

class ManejadorDisparoTest {

//    private Tablero tableroRival;
//    private Jugador jugadorRival;
//    private Nave nave;
//    private Casilla casilla1, casilla2, casilla3;
//    private Casilla casillaSinNave;
//
//    @BeforeEach
//    void setUp() {
//        // Configurar tablero
//        tableroRival = new Tablero(); // Supongo un constructor que define las dimensiones del tablero.
//
//        // Crear una nave y asignarla a una casilla
//        nave = new Crucero(); // Nave con tamaño 3
//        // Crear casillas consecutivas para la nave
//        casilla1 = new Casilla(new Coordenada(2, 3));
//        casilla2 = new Casilla(new Coordenada(2, 4));
//        casilla3 = new Casilla(new Coordenada(2, 5));
//
//        // Asignar la nave a las casillas
//        casilla1.setNave(nave);
//        casilla2.setNave(nave);
//        casilla3.setNave(nave);
//
//        // Colocar las casillas en el tablero
//        tableroRival.setCasilla(casilla1);
//        tableroRival.setCasilla(casilla2);
//        tableroRival.setCasilla(casilla3);
//
//        // Crear una casilla sin nave
//        casillaSinNave = new Casilla(new Coordenada(5, 5));
//        tableroRival.setCasilla(casillaSinNave);
//
//        // Crear jugador rival y asignar la nave a su lista
//        jugadorRival = new Jugador();
//        jugadorRival.setNombre("Pepe");
//        jugadorRival.setNaves(new ArrayList<>());
//        jugadorRival.getNaves().add(nave);
//    }
//
//    @Test
//    void testDisparoACasillaSinNave() {
//        // Configurar disparo
//        DisparoEvento disparoEvento = new DisparoEvento(new Coordenada(5, 5));
//        ManejadorDisparo manejador = new ManejadorDisparo(disparoEvento, tableroRival, jugadorRival);
//
//        // Procesar evento
//        ResultadoDisparoEvento resultado = manejador.procesarEvento();
//
//        // Verificar resultado
//        assertFalse(resultado.getTablero().getCasilla(new Coordenada(5, 5)).getNave().isPresent());
//        assertEquals(EstadoCasilla.GOLPEADA, casillaSinNave.getEstado());
//    }
//
//    @Test
//    void testDisparoACasillaConNave() {
//        // Configurar disparo
//        DisparoEvento disparoEvento = new DisparoEvento(new Coordenada(2, 3));
//        ManejadorDisparo manejador = new ManejadorDisparo(disparoEvento, tableroRival, jugadorRival);
//
//        // Procesar evento
//        ResultadoDisparoEvento resultado = manejador.procesarEvento();
//
//        // Verificar resultado
//        assertTrue(resultado.getTablero().getCasilla(new Coordenada(2, 3)).getNave().isPresent());
//        assertEquals(EstadoCasilla.GOLPEADA, casilla1.getEstado());
//        assertEquals(1, nave.getImpactos());
//        assertEquals(EstadoNave.AVERIADA, nave.getEstadoNave());
//    }
//
//    @Test
//    void testDisparoQueHundeLaNave() {
//        // Configurar impactos para que el siguiente disparo la hunda
//        nave.recibirImpacto();
//        nave.recibirImpacto();
//
//        // Configurar disparo
//        DisparoEvento disparoEvento = new DisparoEvento(new Coordenada(2, 3));
//        ManejadorDisparo manejador = new ManejadorDisparo(disparoEvento, tableroRival, jugadorRival);
//
//        // Procesar evento
//        ResultadoDisparoEvento resultado = manejador.procesarEvento();
//
//        // Verificar resultado
//        assertEquals(EstadoCasilla.GOLPEADA, resultado.getTablero().getCasilla(new Coordenada(2, 3)).getEstado());
//        assertEquals(EstadoCasilla.GOLPEADA, casilla1.getEstado());
//        assertEquals(3, nave.getImpactos());
//        assertEquals(EstadoNave.HUNDIDA, nave.getEstadoNave());
//    }
//
//    @Test
//    void testDisparoACasillaYaGolpeada() {
//        // Marcar casilla como golpeada previamente
//        casilla1.setEstado(EstadoCasilla.GOLPEADA);
//
//        // Configurar disparo
//        DisparoEvento disparoEvento = new DisparoEvento(new Coordenada(2, 3));
//        ManejadorDisparo manejador = new ManejadorDisparo(disparoEvento, tableroRival, jugadorRival);
//
//        // Procesar evento y verificar excepción
//        assertThrows(IllegalArgumentException.class, manejador::procesarEvento);
//    }
//
//    @Test
//    void testDisparoConEstadoDeImpactosInvalidos() {
//        // Simular un caso donde la nave tiene más impactos de lo permitido
//        nave.recibirImpacto();
//        nave.recibirImpacto();
//        nave.recibirImpacto();
//        nave.recibirImpacto(); // Exceder impactos
//
//        // Configurar disparo
//        DisparoEvento disparoEvento = new DisparoEvento(new Coordenada(2, 3));
//        ManejadorDisparo manejador = new ManejadorDisparo(disparoEvento, tableroRival, jugadorRival);
//
//        // Procesar evento y verificar excepción
//        assertThrows(IllegalStateException.class, manejador::procesarEvento);
//    }
//
//    @Test
//    void testDisparoACasillasDeNave() {
//        // Configurar disparo
//        DisparoEvento disparoEvento1 = new DisparoEvento(new Coordenada(2, 3)); // Primera casilla de la nave
//        DisparoEvento disparoEvento2 = new DisparoEvento(new Coordenada(2, 4)); // Segunda casilla de la nave
//        DisparoEvento disparoEvento3 = new DisparoEvento(new Coordenada(2, 5)); // Tercera casilla de la nave
//
//        ManejadorDisparo manejador1 = new ManejadorDisparo(disparoEvento1, tableroRival, jugadorRival);
//        ManejadorDisparo manejador2 = new ManejadorDisparo(disparoEvento2, tableroRival, jugadorRival);
//        ManejadorDisparo manejador3 = new ManejadorDisparo(disparoEvento3, tableroRival, jugadorRival);
//
//        // Procesar eventos (disparos)
//        manejador1.procesarEvento();
//        manejador2.procesarEvento();
//        ResultadoDisparoEvento resultado3 = manejador3.procesarEvento(); // Tercer disparo que hundirá la nave
//
//        // Verificar que todas las casillas de la nave estén golpeadas
//        assertEquals(EstadoCasilla.GOLPEADA, casilla1.getEstado());
//        assertEquals(EstadoCasilla.GOLPEADA, casilla2.getEstado());
//        assertEquals(EstadoCasilla.GOLPEADA, casilla3.getEstado());
//
//        // Verificar que la nave esté hundida
//        assertEquals(EstadoNave.HUNDIDA, nave.getEstadoNave());
//
//        // Verificar que el estado de la nave en las casillas también esté actualizado
//        assertEquals(EstadoNave.HUNDIDA, casilla1.getNave().get().getEstadoNave());
//        assertEquals(EstadoNave.HUNDIDA, casilla2.getNave().get().getEstadoNave());
//        assertEquals(EstadoNave.HUNDIDA, casilla3.getNave().get().getEstadoNave());
//
//        // Verificar que el tablero refleje correctamente el estado de las casillas
//        assertAll("Tablero debe tener las casillas actualizadas",
//                () -> assertEquals(EstadoCasilla.GOLPEADA, tableroRival.getCasilla(new Coordenada(2, 3)).getEstado()),
//                () -> assertEquals(EstadoCasilla.GOLPEADA, tableroRival.getCasilla(new Coordenada(2, 4)).getEstado()),
//                () -> assertEquals(EstadoCasilla.GOLPEADA, tableroRival.getCasilla(new Coordenada(2, 5)).getEstado())
//        );
//
//        // Verificar el resultado
//        assertTrue(resultado3.getTablero().getCasilla(new Coordenada(2, 5)).getNave().isPresent());
//    }
//
//    @Test
//    void testNaveHundidaActualizaCasillasCorrectamente() {
//        // Crear un manejador para disparar a cada casilla de la nave
//        DisparoEvento disparoEvento1 = new DisparoEvento(new Coordenada(2, 3)); // Primera casilla de la nave
//        DisparoEvento disparoEvento2 = new DisparoEvento(new Coordenada(2, 4)); // Segunda casilla de la nave
//        DisparoEvento disparoEvento3 = new DisparoEvento(new Coordenada(2, 5)); // Tercera casilla de la nave
//
//        ManejadorDisparo manejador1 = new ManejadorDisparo(disparoEvento1, tableroRival, jugadorRival);
//        ManejadorDisparo manejador2 = new ManejadorDisparo(disparoEvento2, tableroRival, jugadorRival);
//        ManejadorDisparo manejador3 = new ManejadorDisparo(disparoEvento3, tableroRival, jugadorRival);
//
//        // Procesar los disparos
//        manejador1.procesarEvento();
//        manejador2.procesarEvento();
//        manejador3.procesarEvento(); // Este último debería hundir la nave
//
//        // Verificar que el estado de las casillas se haya actualizado a GOLPEADA
//        assertAll("Casillas de nave deben estar golpeadas",
//                () -> assertEquals(EstadoCasilla.GOLPEADA, casilla1.getEstado()),
//                () -> assertEquals(EstadoCasilla.GOLPEADA, casilla2.getEstado()),
//                () -> assertEquals(EstadoCasilla.GOLPEADA, casilla3.getEstado())
//        );
//
//        // Verificar que las naves dentro de las casillas también estén hundidas
//        assertAll("Naves en las casillas deben estar hundidas",
//                () -> assertEquals(EstadoNave.HUNDIDA, casilla1.getNave().get().getEstadoNave()),
//                () -> assertEquals(EstadoNave.HUNDIDA, casilla2.getNave().get().getEstadoNave()),
//                () -> assertEquals(EstadoNave.HUNDIDA, casilla3.getNave().get().getEstadoNave())
//        );
//
//        // Verificar que el tablero refleje correctamente el estado de las casillas
//        assertAll("Tablero debe tener las casillas actualizadas",
//                () -> assertEquals(EstadoCasilla.GOLPEADA, tableroRival.getCasilla(new Coordenada(2, 3)).getEstado()),
//                () -> assertEquals(EstadoCasilla.GOLPEADA, tableroRival.getCasilla(new Coordenada(2, 4)).getEstado()),
//                () -> assertEquals(EstadoCasilla.GOLPEADA, tableroRival.getCasilla(new Coordenada(2, 5)).getEstado())
//        );
//    }


}