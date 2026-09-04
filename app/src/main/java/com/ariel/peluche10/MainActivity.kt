package com.ariel.peluche10

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

private const val PELUCHE_SIZE = 190f
private const val BALL_SIZE = 66f
private const val SHOT_BALL_SIZE = 44f
private const val PORTERO_SIZE = 94f

private data class EquipoMundial(val nombre: String, val principal: Color, val detalle: Color)

private val seleccionesMundial2026 = listOf(
    EquipoMundial("Mexico", Color(0xFF08783B), Color.White),
    EquipoMundial("Canada", Color(0xFFD62828), Color.White),
    EquipoMundial("Estados Unidos", Color(0xFF1E4D9B), Color.White),
    EquipoMundial("Argentina", Color(0xFF71C7EC), Color.White),
    EquipoMundial("Brasil", Color(0xFFFFD21F), Color(0xFF17833B)),
    EquipoMundial("Colombia", Color(0xFFFFD226), Color(0xFF244A9A)),
    EquipoMundial("Ecuador", Color(0xFFFFD329), Color(0xFF263B87)),
    EquipoMundial("Paraguay", Color(0xFFD62828), Color.White),
    EquipoMundial("Uruguay", Color(0xFF63BEEB), Color.White),
    EquipoMundial("Australia", Color(0xFFFFC400), Color(0xFF16754A)),
    EquipoMundial("Iran", Color.White, Color(0xFF149143)),
    EquipoMundial("Japon", Color(0xFF1C3D87), Color(0xFFD62C2C)),
    EquipoMundial("Jordania", Color.White, Color(0xFFB5222A)),
    EquipoMundial("Corea del Sur", Color(0xFFE63946), Color(0xFF1C4E9A)),
    EquipoMundial("Qatar", Color(0xFF7A1733), Color.White),
    EquipoMundial("Arabia Saudi", Color(0xFF087B3B), Color.White),
    EquipoMundial("Uzbekistan", Color(0xFF1687C4), Color.White),
    EquipoMundial("Irak", Color.White, Color(0xFF16934A)),
    EquipoMundial("Argelia", Color.White, Color(0xFF168C46)),
    EquipoMundial("Cabo Verde", Color(0xFF174E9B), Color(0xFFE72F38)),
    EquipoMundial("Costa de Marfil", Color(0xFFF28C28), Color.White),
    EquipoMundial("Egipto", Color(0xFFE33636), Color.White),
    EquipoMundial("Ghana", Color.White, Color(0xFF16884C)),
    EquipoMundial("Marruecos", Color(0xFFD42D2D), Color(0xFF177A46)),
    EquipoMundial("Senegal", Color.White, Color(0xFF168E4C)),
    EquipoMundial("Sudafrica", Color(0xFFF4C51D), Color(0xFF198547)),
    EquipoMundial("Tunez", Color.White, Color(0xFFD72E2E)),
    EquipoMundial("RD Congo", Color(0xFF1681C5), Color(0xFFE1B92B)),
    EquipoMundial("Curazao", Color(0xFF1B5EA6), Color(0xFFF2C51D)),
    EquipoMundial("Haiti", Color(0xFF1A4E9A), Color(0xFFE43737)),
    EquipoMundial("Panama", Color.White, Color(0xFFD62C35)),
    EquipoMundial("Nueva Zelanda", Color.Black, Color.White),
    EquipoMundial("Austria", Color.White, Color(0xFFD72E35)),
    EquipoMundial("Belgica", Color(0xFFD92B2B), Color(0xFFF3C41D)),
    EquipoMundial("Bosnia y Herzegovina", Color(0xFF1E5BA8), Color.White),
    EquipoMundial("Croacia", Color.White, Color(0xFFD42A36)),
    EquipoMundial("Chequia", Color(0xFFD32F35), Color.White),
    EquipoMundial("Inglaterra", Color.White, Color(0xFF1D4E9B)),
    EquipoMundial("Francia", Color(0xFF1A3D89), Color(0xFFE33640)),
    EquipoMundial("Alemania", Color.White, Color.Black),
    EquipoMundial("Paises Bajos", Color(0xFFF47B2A), Color.Black),
    EquipoMundial("Noruega", Color(0xFFD72833), Color.White),
    EquipoMundial("Portugal", Color(0xFFCF2937), Color(0xFF167B43)),
    EquipoMundial("Escocia", Color(0xFF1B4E98), Color.White),
    EquipoMundial("Espana", Color(0xFFE13232), Color(0xFFF4C61F)),
    EquipoMundial("Suecia", Color(0xFFF4C61F), Color(0xFF1D5B9E)),
    EquipoMundial("Suiza", Color(0xFFD72E35), Color.White),
    EquipoMundial("Turquia", Color(0xFFD62D35), Color.White)
)

private fun recursoPeluche(equipo: EquipoMundial): Int = when (equipo.nombre) {
    "Mexico" -> R.drawable.peluche_mexico
    "Canada" -> R.drawable.peluche_canada
    "Estados Unidos" -> R.drawable.peluche_estados_unidos
    "Argentina" -> R.drawable.peluche_argentina
    "Brasil" -> R.drawable.peluche_brasil
    "Colombia" -> R.drawable.peluche_colombia
    "Ecuador" -> R.drawable.peluche_ecuador
    "Paraguay" -> R.drawable.peluche_paraguay
    "Uruguay" -> R.drawable.peluche_uruguay
    "Australia" -> R.drawable.peluche_australia
    "Iran" -> R.drawable.peluche_iran
    "Japon" -> R.drawable.peluche_japon
    "Jordania" -> R.drawable.peluche_jordania
    "Corea del Sur" -> R.drawable.peluche_corea_sur
    "Qatar" -> R.drawable.peluche_qatar
    "Arabia Saudi" -> R.drawable.peluche_arabia_saudi
    "Uzbekistan" -> R.drawable.peluche_uzbekistan
    "Irak" -> R.drawable.peluche_irak
    "Argelia" -> R.drawable.peluche_argelia
    "Cabo Verde" -> R.drawable.peluche_cabo_verde
    "Costa de Marfil" -> R.drawable.peluche_costa_marfil
    "Egipto" -> R.drawable.peluche_egipto
    "Ghana" -> R.drawable.peluche_ghana
    "Marruecos" -> R.drawable.peluche_marruecos
    "Senegal" -> R.drawable.peluche_senegal
    "Sudafrica" -> R.drawable.peluche_sudafrica
    "Tunez" -> R.drawable.peluche_tunez
    "RD Congo" -> R.drawable.peluche_rd_congo
    "Curazao" -> R.drawable.peluche_curazao
    "Haiti" -> R.drawable.peluche_haiti
    "Panama" -> R.drawable.peluche_panama
    "Nueva Zelanda" -> R.drawable.peluche_nueva_zelanda
    "Austria" -> R.drawable.peluche_austria
    "Belgica" -> R.drawable.peluche_belgica
    "Bosnia y Herzegovina" -> R.drawable.peluche_bosnia
    "Croacia" -> R.drawable.peluche_croacia
    "Chequia" -> R.drawable.peluche_chequia
    "Inglaterra" -> R.drawable.peluche_inglaterra
    "Francia" -> R.drawable.peluche_francia
    "Alemania" -> R.drawable.peluche_alemania
    "Paises Bajos" -> R.drawable.peluche_paises_bajos
    "Noruega" -> R.drawable.peluche_noruega
    "Portugal" -> R.drawable.peluche_portugal
    "Escocia" -> R.drawable.peluche_escocia
    "Espana" -> R.drawable.peluche_espana
    "Suecia" -> R.drawable.peluche_suecia
    "Suiza" -> R.drawable.peluche_suiza
    "Turquia" -> R.drawable.peluche_turquia
    else -> R.drawable.peluche_sin_circulo
}

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gravedad = FloatArray(3)
    private var ultimoGesto = 0L
    private var gestoTelefono by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        setContent { Peluche10App(gestoTelefono) }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent) {
        val ahora = System.currentTimeMillis()
        for (i in 0..2) gravedad[i] = gravedad[i] * 0.82f + event.values[i] * 0.18f
        val lateral = kotlin.math.hypot(event.values[0] - gravedad[0], event.values[1] - gravedad[1])
        val vertical = kotlin.math.abs(event.values[2] - gravedad[2])
        if (ahora - ultimoGesto < 2_500) return
        when {
            vertical > 15f -> { gestoTelefono = if (gestoTelefono <= 0) 1 else gestoTelefono + 1; ultimoGesto = ahora }
            lateral > 16f -> { gestoTelefono = if (gestoTelefono >= 0) -1 else gestoTelefono - 1; ultimoGesto = ahora }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

@Composable
private fun Peluche10App(gestoTelefono: Int) {
    var seccion by remember { mutableIntStateOf(0) }
    var jugando by remember { mutableStateOf(true) }
    var pausado by remember { mutableStateOf(false) }
    var saltando by remember { mutableStateOf(false) }
    var equipo by remember { mutableStateOf(seleccionesMundial2026.first()) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5FAFF)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Peluche 10", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text(if (seccion == 1) "Tiros" else if (seccion == 2) "Ropa" else if (pausado) "En pausa" else if (jugando) "Jugando" else "Caminando", color = Color(0xFF386891))
                }

                TabRow(selectedTabIndex = seccion) {
                    Tab(selected = seccion == 0, onClick = { seccion = 0 }, text = { Text("Mascota") })
                    Tab(selected = seccion == 1, onClick = { seccion = 1 }, text = { Text("Tiros") })
                    Tab(selected = seccion == 2, onClick = { seccion = 2 }, text = { Text("Ropa") })
                }

                if (seccion == 0) {
                    CampoDeJuego(
                        modifier = Modifier.weight(1f),
                        jugando = jugando,
                        pausado = pausado,
                        saltando = saltando,
                        mostrarPorteria = false,
                        gestoTelefono = gestoTelefono,
                        equipo = equipo,
                        alTerminarSalto = { saltando = false },
                        alAnotar = {}
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ControlButton("Jugar", Icons.Outlined.SportsSoccer, jugando) { jugando = true; pausado = false }
                        ControlButton("Caminar", Icons.Outlined.DirectionsWalk, !jugando) { jugando = false; pausado = false }
                        ControlButton("Ropa", Icons.Default.KeyboardArrowUp, seccion == 2) { seccion = 2 }
                        ControlButton(if (pausado) "Seguir" else "Pausa", if (pausado) Icons.Default.PlayArrow else Icons.Default.Pause, pausado) { pausado = !pausado }
                    }
                } else if (seccion == 1) {
                    CampoTiros(modifier = Modifier.weight(1f), gestoTelefono = gestoTelefono, equipo = equipo)
                } else {
                    VestimentaPreview(
                        modifier = Modifier.weight(1f),
                        equipo = equipo,
                        alSeleccionar = { equipo = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun CampoDeJuego(
    modifier: Modifier,
    jugando: Boolean,
    pausado: Boolean,
    saltando: Boolean,
    mostrarPorteria: Boolean,
    gestoTelefono: Int,
    equipo: EquipoMundial,
    alTerminarSalto: () -> Unit,
    alAnotar: () -> Unit
) {
    val density = LocalDensity.current
    var pelucheX by remember { mutableFloatStateOf(90f) }
    var pelucheY by remember { mutableFloatStateOf(170f) }
    var pelucheVx by remember { mutableFloatStateOf(64f) }
    var pelucheVy by remember { mutableFloatStateOf(43f) }
    var balonX by remember { mutableFloatStateOf(40f) }
    var balonY by remember { mutableFloatStateOf(80f) }
    var balonVx by remember { mutableFloatStateOf(110f) }
    var balonVy by remember { mutableFloatStateOf(82f) }
    var tiempo by remember { mutableFloatStateOf(0f) }
    var inicioSalto by remember { mutableFloatStateOf(-10f) }
    var reaccion by remember { mutableStateOf("normal") }
    var ultimoGestoVisto by remember { mutableIntStateOf(gestoTelefono) }

    LaunchedEffect(gestoTelefono) {
        if (gestoTelefono == ultimoGestoVisto) return@LaunchedEffect
        ultimoGestoVisto = gestoTelefono
        reaccion = if (gestoTelefono > 0) "llorando" else "enojado"
        delay(900)
        reaccion = "normal"
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
            .background(Color(0xFFDFF2FF), RoundedCornerShape(8.dp))
    ) {
        FondoEstadio(Modifier.matchParentSize())
        val ancho = maxWidth.value
        val alto = maxHeight.value
        val limiteX = max(0f, ancho - PELUCHE_SIZE)
        val limiteY = max(0f, alto - PELUCHE_SIZE)
        val limiteBalonX = max(0f, ancho - BALL_SIZE)
        val limiteBalonY = max(0f, alto - BALL_SIZE)

        LaunchedEffect(jugando, pausado, ancho, alto) {
            while (isActive) {
                delay(16)
                val delta = 0.016f
                if (!pausado && ancho > PELUCHE_SIZE && alto > PELUCHE_SIZE) {
                    tiempo += delta

                    if (jugando) {
                        // Nunca dejes la pelota detenida tras un arrastre o choque.
                        if (abs(balonVx) < 90f) {
                            balonVx = if (balonX < limiteBalonX / 2f) 160f else -160f
                        }
                        if (abs(balonVy) < 80f) {
                            balonVy = if (balonY < limiteBalonY / 2f) 135f else -135f
                        }
                        balonX += balonVx * delta
                        balonY += balonVy * delta
                        if (balonX <= 0f) {
                            balonX = 0f
                            balonVx = abs(balonVx).coerceAtLeast(135f)
                        } else if (balonX >= limiteBalonX) {
                            balonX = limiteBalonX
                            balonVx = -abs(balonVx).coerceAtLeast(135f)
                        }
                        if (balonY <= 0f) {
                            balonY = 0f
                            balonVy = abs(balonVy).coerceAtLeast(120f)
                        } else if (balonY >= limiteBalonY) {
                            balonY = limiteBalonY
                            balonVy = -abs(balonVy).coerceAtLeast(120f)
                        }

                        val centroPelucheX = pelucheX + PELUCHE_SIZE / 2f
                        val centroPelucheY = pelucheY + PELUCHE_SIZE / 2f
                        val centroBalonX = balonX + BALL_SIZE / 2f
                        val centroBalonY = balonY + BALL_SIZE / 2f
                        val dx = centroBalonX - centroPelucheX
                        val dy = centroBalonY - centroPelucheY
                        val distancia = max(1f, hypot(dx, dy))
                        val distanciaSegura = 112f
                        if (distancia > distanciaSegura) {
                            val avance = min(116f * delta, distancia - distanciaSegura)
                            pelucheX = (pelucheX + dx / distancia * avance).coerceIn(0f, limiteX)
                            pelucheY = (pelucheY + dy / distancia * avance).coerceIn(0f, limiteY)
                        }

                        if (mostrarPorteria && balonX > limiteBalonX - 30f && balonY < 116f) {
                            alAnotar()
                            balonX = 45f
                            balonY = alto * 0.65f
                            balonVx = -145f
                            balonVy = 98f
                        }
                    } else {
                        pelucheX += pelucheVx * delta
                        pelucheY += pelucheVy * delta
                        if (pelucheX <= 0f || pelucheX >= limiteX) {
                            pelucheX = pelucheX.coerceIn(0f, limiteX)
                            pelucheVx *= -1f
                        }
                        if (pelucheY <= 0f || pelucheY >= limiteY) {
                            pelucheY = pelucheY.coerceIn(0f, limiteY)
                            pelucheVy *= -1f
                        }
                    }

                    if (saltando && inicioSalto < 0f) inicioSalto = tiempo
                    if (!saltando) inicioSalto = -10f
                    if (inicioSalto >= 0f && tiempo - inicioSalto > 0.72f) alTerminarSalto()
                }
            }
        }

        val salto = if (inicioSalto >= 0f) {
            val progreso = ((tiempo - inicioSalto) / 0.72f).coerceIn(0f, 1f)
            -58f * (1f - abs(2f * progreso - 1f))
        } else 0f
        val balanceo = if (pausado) 0f else sin(tiempo * 7f) * 3f

        if (mostrarPorteria) Porteria(modifier = Modifier.align(Alignment.TopEnd).padding(top = 16.dp, end = 12.dp))

        val mascota = when (reaccion) {
            "llorando" -> R.drawable.sprite_llorando_realista
            "enojado" -> R.drawable.sprite_enojado_referencia
            else -> recursoPeluche(equipo)
        }
        Image(
            painter = painterResource(mascota),
            contentDescription = "Peluche 10 caminando",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(PELUCHE_SIZE.dp).offset(x = pelucheX.dp, y = (pelucheY + salto + balanceo).dp)
        )

        if (jugando) {
            Image(
                painter = painterResource(R.drawable.balon_peluche),
                contentDescription = "Balon que rebota",
                modifier = Modifier.size(BALL_SIZE.dp).offset(x = balonX.dp, y = balonY.dp)
                    .pointerInput(density) {
                        detectDragGestures(onDragEnd = {
                            if (abs(balonVx) < 80f) balonVx = if (balonVx < 0f) -150f else 150f
                            if (abs(balonVy) < 80f) balonVy = if (balonVy < 0f) -130f else 130f
                        }) { change, dragAmount ->
                            change.consume()
                            balonX = (balonX + dragAmount.x / density.density).coerceIn(0f, limiteBalonX)
                            balonY = (balonY + dragAmount.y / density.density).coerceIn(0f, limiteBalonY)
                            balonVx = dragAmount.x / density.density * 7f
                            balonVy = dragAmount.y / density.density * 7f
                        }
                    }
            )
        }
    }
}

@Composable
private fun CampoTiros(modifier: Modifier, gestoTelefono: Int, equipo: EquipoMundial) {
    val density = LocalDensity.current
    var balonX by remember { mutableFloatStateOf(140f) }
    var balonY by remember { mutableFloatStateOf(450f) }
    var velocidadX by remember { mutableFloatStateOf(0f) }
    var velocidadY by remember { mutableFloatStateOf(0f) }
    var enVuelo by remember { mutableStateOf(false) }
    var inicioX by remember { mutableFloatStateOf(0f) }
    var inicioY by remember { mutableFloatStateOf(0f) }
    var goles by remember { mutableIntStateOf(0) }
    var atajadas by remember { mutableIntStateOf(0) }
    var reaccion by remember { mutableStateOf("normal") }
    var mensaje by remember { mutableStateOf("Listo") }
    var porteroX by remember { mutableFloatStateOf(0f) }
    var tiempoPortero by remember { mutableFloatStateOf(0f) }
    var numeroTiro by remember { mutableIntStateOf(0) }
    var errorPortero by remember { mutableFloatStateOf(0f) }
    var celebrandoGol by remember { mutableStateOf(false) }
    var progresoConfeti by remember { mutableFloatStateOf(0f) }
    var ultimoGestoVisto by remember { mutableIntStateOf(gestoTelefono) }

    LaunchedEffect(gestoTelefono) {
        if (gestoTelefono == ultimoGestoVisto) return@LaunchedEffect
        ultimoGestoVisto = gestoTelefono
        if (gestoTelefono > 0) {
            reaccion = "llorando"
            mensaje = "No me sacudas"
        } else {
            reaccion = "enojado"
            mensaje = "Estoy enojado"
        }
        delay(900)
        reaccion = "normal"
        if (!enVuelo) mensaje = "Listo"
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
            .background(Color(0xFFDFF2FF), RoundedCornerShape(8.dp))
    ) {
        val ancho = maxWidth.value
        val alto = maxHeight.value
        val limiteX = max(0f, ancho - SHOT_BALL_SIZE)
        val limiteY = max(0f, alto - SHOT_BALL_SIZE)
        val porteriaIzquierda = 24f
        val porteriaDerecha = max(porteriaIzquierda, ancho - 24f)
        val limitePorteroIzq = porteriaIzquierda + 2f
        val limitePorteroDer = max(limitePorteroIzq, porteriaDerecha - PORTERO_SIZE - 2f)
        val puntoPenalX = ((ancho - SHOT_BALL_SIZE) / 2f).coerceIn(0f, limiteX)
        val puntoPenalY = (alto * 0.66f).coerceIn(170f, limiteY)

        fun reiniciar(nuevoMensaje: String = "Listo") {
            balonX = puntoPenalX
            balonY = puntoPenalY
            velocidadX = 0f
            velocidadY = 0f
            enVuelo = false
            reaccion = "normal"
            mensaje = nuevoMensaje
        }

        fun anotarGol() {
            goles++
            balonX = if (porteroX < (ancho - PORTERO_SIZE) / 2f) {
                porteriaDerecha - SHOT_BALL_SIZE - 14f
            } else {
                porteriaIzquierda + 14f
            }
            balonY = 106f
            velocidadX = 0f
            velocidadY = 0f
            enVuelo = false
            reaccion = "normal"
            mensaje = "GOOOOL!"
            progresoConfeti = 0f
            celebrandoGol = true
        }

        LaunchedEffect(celebrandoGol) {
            if (!celebrandoGol) return@LaunchedEffect
            val inicio = withFrameNanos { it }
            while (isActive && progresoConfeti < 1f) {
                withFrameNanos { ahora ->
                    progresoConfeti = ((ahora - inicio) / 1_600_000_000f).coerceIn(0f, 1f)
                }
            }
            if (isActive) {
                celebrandoGol = false
                progresoConfeti = 0f
                reiniciar()
            }
        }

        LaunchedEffect(ancho, alto) {
            var anterior = 0L
            while (isActive) {
                withFrameNanos { ahora ->
                    if (anterior == 0L) anterior = ahora
                    val delta = min(0.035f, (ahora - anterior) / 1_000_000_000f)
                    anterior = ahora
                    tiempoPortero += delta
                    if (celebrandoGol) return@withFrameNanos
                    if (!enVuelo) {
                        val centroPorteria = (limitePorteroIzq + limitePorteroDer) / 2f
                        val recorrido = (limitePorteroDer - limitePorteroIzq) / 2f
                        porteroX = (centroPorteria + sin(tiempoPortero * 2.1f) * recorrido)
                            .coerceIn(limitePorteroIzq, limitePorteroDer)
                        return@withFrameNanos
                    }

                    val objetivoPortero = (balonX + SHOT_BALL_SIZE / 2f - PORTERO_SIZE / 2f + errorPortero)
                        .coerceIn(limitePorteroIzq, limitePorteroDer)
                    porteroX += (objetivoPortero - porteroX).coerceIn(-180f * delta, 180f * delta)

                    balonX += velocidadX * delta
                    balonY += velocidadY * delta
                    velocidadY += 225f * delta

                    val centroBalonX = balonX + SHOT_BALL_SIZE / 2f
                    val centroPorteroX = porteroX + PORTERO_SIZE / 2f
                    val laAtrapó = balonY in 42f..175f && abs(centroBalonX - centroPorteroX) < 53f
                    val entroEnPorteria = balonX in porteriaIzquierda..porteriaDerecha && balonY <= 50f && velocidadY < 0f
                    if (laAtrapó) {
                        atajadas++
                        reiniciar("Parada!")
                    } else if (entroEnPorteria) {
                        anotarGol()
                    } else if (balonX <= 0f || balonX >= limiteX || balonY < -16f) {
                        reiniciar("Fallaste")
                    } else if (balonY >= limiteY) {
                        reiniciar("Casi")
                    }
                }
            }
        }

        FondoEstadio(Modifier.matchParentSize())

        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 7.dp.toPx(),
                center = Offset(
                    (puntoPenalX + SHOT_BALL_SIZE / 2f) * density.density,
                    (puntoPenalY + SHOT_BALL_SIZE / 2f) * density.density
                )
            )
        }

        LaunchedEffect(ancho) {
            reiniciar()
            porteroX = ((porteriaIzquierda + porteriaDerecha - PORTERO_SIZE) / 2f)
                .coerceIn(limitePorteroIzq, limitePorteroDer)
        }

        Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tiros", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text("$goles goles  $atajadas paradas", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
            Text(mensaje, style = MaterialTheme.typography.labelMedium, color = Color.White)
        }

        PorteriaJuego(
            modifier = Modifier.fillMaxWidth().height(142.dp)
                .align(Alignment.TopCenter).padding(top = 52.dp, start = 20.dp, end = 20.dp)
        )

        val mascota = when (reaccion) {
            "llorando" -> R.drawable.sprite_llorando_realista
            "enojado" -> R.drawable.sprite_enojado_referencia
            else -> recursoPeluche(equipo)
        }
        Image(
            painter = painterResource(mascota),
            contentDescription = "Peluche portero",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(PORTERO_SIZE.dp).offset(x = porteroX.dp, y = 66.dp)
        )

        Image(
            painter = painterResource(R.drawable.balon_peluche),
            contentDescription = "Balon para tirar",
            modifier = Modifier.size(SHOT_BALL_SIZE.dp).offset(x = balonX.dp, y = balonY.dp)
                .pointerInput(density, ancho, alto) {
                    detectDragGestures(
                        onDragStart = {
                            inicioX = balonX
                            inicioY = balonY
                            enVuelo = false
                            reaccion = "normal"
                            mensaje = "Tira"
                            numeroTiro++
                            errorPortero = when (numeroTiro % 3) {
                                0 -> 0f
                                1 -> -42f
                                else -> 38f
                            }
                        },
                        onDragEnd = {
                            val impulsoX = balonX - inicioX
                            val impulsoY = balonY - inicioY
                            val centroBalonX = balonX + SHOT_BALL_SIZE / 2f
                            val dentroDePorteria = centroBalonX in (porteriaIzquierda + 8f)..(porteriaDerecha - 8f) && balonY <= 174f
                            if (dentroDePorteria) {
                                anotarGol()
                            } else if (impulsoY < -10f) {
                                velocidadX = (impulsoX * 2.7f).coerceIn(-500f, 500f)
                                velocidadY = (impulsoY * 2.8f).coerceIn(-860f, -300f)
                                enVuelo = true
                            } else {
                                reiniciar("Tiro muy corto")
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        balonX = (balonX + dragAmount.x / density.density).coerceIn(0f, limiteX)
                        balonY = (balonY + dragAmount.y / density.density).coerceIn(0f, limiteY)
                    }
                }
        )

        if (celebrandoGol) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val colores = listOf(Color(0xFFFFD43B), Color(0xFFE74C3C), Color(0xFF2F8FE6), Color(0xFF6CC26B))
                repeat(36) { indice ->
                    val x = 18f + (indice * 47f) % (size.width - 36f)
                    val inicioY = 72f + (indice % 6) * 17f
                    val y = inicioY + progresoConfeti * (130f + (indice % 5) * 33f)
                    drawRect(colores[indice % colores.size], topLeft = Offset(x, y), size = androidx.compose.ui.geometry.Size(10f, 16f))
                }
            }
            Text(
                "GOOOOL!",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD43B)
            )
        }
    }
}

@Composable
private fun FondoEstadio(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val gradas = Color(0xFF173E62)
        val pastoClaro = Color(0xFF58A85D)
        val pastoOscuro = Color(0xFF41934D)
        drawRect(gradas, size = size.copy(height = size.height * 0.22f))
        val filas = 5
        repeat(filas) { fila ->
            val y = 16f + fila * 16f
            repeat(18) { columna ->
                val color = if ((fila + columna) % 3 == 0) Color(0xFFF7D15B) else Color(0xFFC8E4F3)
                drawCircle(color, radius = 3.2f, center = Offset(14f + columna * (size.width - 28f) / 17f, y))
            }
        }
        val inicioCampo = size.height * 0.18f
        drawRect(
            pastoClaro,
            topLeft = Offset(0f, inicioCampo),
            size = size.copy(height = size.height - inicioCampo)
        )
        repeat(7) { franja ->
            if (franja % 2 == 0) {
                drawRect(
                    pastoOscuro.copy(alpha = 0.34f),
                    topLeft = Offset(0f, inicioCampo + franja * (size.height - inicioCampo) / 7f),
                    size = size.copy(height = (size.height - inicioCampo) / 7f)
                )
            }
        }
        drawLine(Color.White.copy(alpha = 0.7f), Offset(0f, inicioCampo), Offset(size.width, inicioCampo), 2f)
    }
}

@Composable
private fun VestimentaPreview(modifier: Modifier) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
            .background(Color(0xFFDCF1FF), RoundedCornerShape(8.dp))
    ) {
        FondoEstadio(Modifier.matchParentSize())
        Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
            Text("Vestimenta", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Próximamente", style = MaterialTheme.typography.labelMedium, color = Color.White)
        }
        Image(
            painter = painterResource(R.drawable.peluche_sin_circulo),
            contentDescription = "Uniforme actual de Peluche 10",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(230.dp).align(Alignment.Center)
        )
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MuestraUniforme(Color(0xFF074B9C), "Local")
            MuestraUniforme(Color(0xFFFFC928), "Portero")
            MuestraUniforme(Color(0xFFD94242), "Visita")
        }
    }
}

@Composable
private fun MuestraUniforme(color: Color, nombre: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(46.dp)) {
            drawCircle(Color.White, radius = size.minDimension / 2f)
            drawCircle(color, radius = size.minDimension / 2f - 5f)
        }
        Text(nombre, style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Composable
private fun VestimentaPreview(
    modifier: Modifier,
    equipo: EquipoMundial,
    alSeleccionar: (EquipoMundial) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
            .background(Color(0xFFDCF1FF), RoundedCornerShape(8.dp))
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(265.dp)) {
            Image(
                painter = painterResource(R.drawable.fondo_vestidor),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Uniformes Mundial 2026", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text(equipo.nombre, style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
            Image(
                painter = painterResource(recursoPeluche(equipo)),
                contentDescription = "Peluche con uniforme de ${equipo.nombre}",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(210.dp).align(Alignment.Center)
            )
        }
        Text(
            "Elige una seleccion",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF173F62)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(seleccionesMundial2026, key = { it.nombre }) { seleccion ->
                Button(
                    onClick = { alSeleccionar(seleccion) },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (seleccion == equipo) seleccion.principal else Color.White,
                        contentColor = if (seleccion == equipo) Color.White else Color(0xFF163E61)
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Canvas(modifier = Modifier.size(18.dp)) {
                            drawCircle(seleccion.principal, radius = size.minDimension / 2f)
                            drawCircle(seleccion.detalle, radius = size.minDimension / 2f - 4f)
                        }
                        Text(seleccion.nombre, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun PorteriaJuego(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val poste = Color.White
        val red = Color(0xFFC94040)
        val anchoRed = 3f
        drawLine(poste, Offset(8f, 8f), Offset(size.width - 8f, 8f), 9f, StrokeCap.Round)
        drawLine(poste, Offset(8f, 8f), Offset(8f, size.height - 8f), 9f, StrokeCap.Round)
        drawLine(poste, Offset(size.width - 8f, 8f), Offset(size.width - 8f, size.height - 8f), 9f, StrokeCap.Round)
        repeat(8) { fila ->
            val y = 18f + fila * (size.height - 28f) / 8f
            drawLine(red.copy(alpha = 0.65f), Offset(12f, y), Offset(size.width - 12f, y), anchoRed)
        }
        repeat(14) { columna ->
            val x = 14f + columna * (size.width - 28f) / 14f
            drawLine(red.copy(alpha = 0.65f), Offset(x, 12f), Offset(x, size.height - 10f), anchoRed)
        }
    }
}

@Composable
private fun Porteria(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(width = 118.dp, height = 84.dp)) {
        val blanco = Color.White
        val azul = Color(0xFF2C699A)
        drawLine(blanco, Offset(8f, 8f), Offset(size.width - 8f, 8f), 8f, StrokeCap.Round)
        drawLine(blanco, Offset(8f, 8f), Offset(8f, size.height - 6f), 8f, StrokeCap.Round)
        drawLine(blanco, Offset(size.width - 8f, 8f), Offset(size.width - 8f, size.height - 6f), 8f, StrokeCap.Round)
        drawRect(azul.copy(alpha = 0.18f), topLeft = Offset(12f, 12f), size = size.copy(width = size.width - 24f, height = size.height - 16f), style = Stroke(3f))
    }
}

@Composable
private fun ControlButton(
    etiqueta: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    seleccionado: Boolean,
    alPulsar: () -> Unit
) {
    Button(
        onClick = alPulsar,
        modifier = Modifier.width(82.dp).height(72.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) Color(0xFF1769AA) else Color.White,
            contentColor = if (seleccionado) Color.White else Color(0xFF1C4160)
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icono, contentDescription = etiqueta)
            Text(etiqueta, style = MaterialTheme.typography.labelSmall)
        }
    }
}
