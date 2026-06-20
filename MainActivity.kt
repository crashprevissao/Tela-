package com.esp32.display

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hoho.android.usbserial.driver.UsbSerialProber
import java.io.IOException

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializa a leitura do Cabo OTG
        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
        
        var statusConexao = "Aguardando conexão do ESP32 via OTG..."
        
        if (availableDrivers.isNotEmpty()) {
            val driver = availableDrivers[0]
            val connection = manager.openDevice(driver.device)
            if (connection != null) {
                val port = driver.ports[0]
                try {
                    port.open(connection)
                    // Configura a velocidade exata de comunicação física
                    port.setParameters(115200, 8, UsbSerialProber.getDefaultProber().toString().toIntOrNull() ?: 1, 0)
                    statusConexao = "ESP32 Conectado! Ajustando tela..."
                    
                    // TODO: Aqui faremos a leitura contínua dos dados no próximo passo
                } catch (e: IOException) {
                    statusConexao = "Erro ao abrir comunicação."
                }
            }
        }

        setContent {
            TelaResponsivaAjustada(statusConexao)
        }
    }
}

@Composable
fun TelaResponsivaAjustada(textoDisplay: String) {
    // FillMaxSize garante que a interface ocupe 100% de qualquer tamanho de celular automaticamente
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)), // Fundo escuro elegante
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Este texto vai escalonar e se adaptar dinamicamente ao espaço do celular
            Text(
                text = textoDisplay,
                color = Color.Green,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
