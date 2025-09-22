package com.example.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.supercompras.ui.theme.Coral
import com.example.supercompras.ui.theme.Marinho
import com.example.supercompras.ui.theme.SuperComprasTheme
import com.example.supercompras.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperComprasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaDeCompras(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ListaDeCompras(modifier: Modifier = Modifier) {
    var listaDeItens by rememberSaveable { mutableStateOf(listOf<ItemCompra>()) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ImagemTopo()
        AdicionarItem(aoSalvarItem = { textoNovo ->
            listaDeItens = listaDeItens + ItemCompra(texto = textoNovo)
        })
        Spacer(modifier = Modifier.padding(48.dp))

        Titulo(texto = "Lista de Compras")

        if (listaDeItens.any { item -> !item.foiComprado }) {
            ListaDeItens(
                lista = listaDeItens,
                aoMudarStatus = { itemSelecionado ->
                    listaDeItens = listaDeItens.map { itemMap ->
                        if (itemSelecionado == itemMap) {
                            itemSelecionado.copy(foiComprado = !itemSelecionado.foiComprado)
                        } else {
                            itemMap
                        }
                    }
                },
                aoRemoverItem = { itemRemovido ->
                    listaDeItens = listaDeItens - itemRemovido
                },
                aoEditarItem = { itemEditado, novoTexto ->
                    listaDeItens = listaDeItens.map { itemAtual ->
                        if (itemAtual == itemEditado) {
                            itemAtual.copy(texto = novoTexto)
                        } else {
                            itemAtual
                        }
                    }
                },
                modifier = Modifier
            )
        }

        Titulo(texto = "Comprado")

        if (listaDeItens.any { item -> item.foiComprado }) {
            ListaDeItens(
                lista = listaDeItens,
                aoMudarStatus = { itemSelecionado ->
                    listaDeItens = listaDeItens.map { itemMap ->
                        if (itemSelecionado == itemMap) {
                            itemSelecionado.copy(foiComprado = !itemSelecionado.foiComprado)
                        } else {
                            itemMap
                        }
                    }
                },
                aoRemoverItem = { itemRemovido ->
                    listaDeItens = listaDeItens - itemRemovido
                },
                aoEditarItem = { itemEditado, novoTexto ->
                    listaDeItens = listaDeItens.map { itemAtual ->
                        if (itemAtual == itemEditado) {
                            itemAtual.copy(texto = novoTexto)
                        } else {
                            itemAtual
                        }
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ListaDeItens(
    lista: List<ItemCompra>,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra, novoTexto: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        lista.forEach { item ->
            ItemDaLista(
                item = item,
                aoMudarStatus = aoMudarStatus,
                aoRemoverItem = aoRemoverItem,
                aoEditarItem = aoEditarItem,
            )
        }
    }
}


@Composable
fun AdicionarItem(aoSalvarItem: (texto: String) -> Unit, modifier: Modifier = Modifier) {
    var texto = rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = texto.value,
        onValueChange = { texto.value = it },
        placeholder = {
            Text(
                text = "Digite o item que deseja adicionar",
                style = Typography.bodyMedium,
                color = Color.Gray
            )
        },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
    )

    Button(
        onClick = {
            aoSalvarItem(texto.value)
            texto.value = ""
        },
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Coral),
        modifier = modifier
    ) {
        Text(
            text = "Salvar item",
            color = Color.White,
            style = Typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(text = texto, style = Typography.headlineLarge, modifier = modifier)
}

@Composable
fun ItemDaLista(
    item: ItemCompra,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra, novoTexto: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.Top, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
        ) {

            var textoEditado by rememberSaveable { mutableStateOf(item.texto) }
            var emEdicao by rememberSaveable { mutableStateOf(false) }

            Checkbox(
                checked = item.foiComprado,
                onCheckedChange = {
                    aoMudarStatus(item)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )

            if (emEdicao) {
                OutlinedTextField(
                    value = textoEditado,
                    onValueChange = { textoEditado = it },
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    modifier = Modifier
                )

                IconButton(
                    onClick = {
                        aoEditarItem(item, textoEditado)
                        emEdicao = false
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icone(
                        icone = Icons.Default.Done,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            } else {
                Text(
                    text = item.texto,
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }

            IconButton(
                onClick = {
                    aoRemoverItem(item)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Icone(
                    icone = Icons.Default.Delete,
                    modifier = Modifier
                        .size(16.dp)
                )
            }

            IconButton(
                onClick = {
                    emEdicao = true
                },
                modifier = Modifier
            ) {
                Icone(
                    icone = Icons.Default.Edit, modifier = Modifier.size(16.dp)
                )
            }
        }
        Text(
            text = "Segunda-feira (31/10/2022) às 08:30",
            style = Typography.labelSmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }

}

@Composable
fun ImagemTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.imagem_topo),
        contentDescription = null,
        modifier.size(160.dp)
    )
}

@Composable
fun Icone(icone: ImageVector, modifier: Modifier = Modifier) {
    Icon(icone, contentDescription = null, tint = Marinho, modifier = modifier)
}

@Preview
@Composable
private fun AdicionarItemPreview() {
    SuperComprasTheme {
        AdicionarItem(aoSalvarItem = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TituloPreview() {
    SuperComprasTheme {
        Titulo(texto = "Lista de Compras")
    }
}

@Preview
@Composable
private fun ImagemTopoPreview() {
    SuperComprasTheme {
        ImagemTopo()
    }
}

@Preview
@Composable
private fun ItemDaListaPreview() {
    SuperComprasTheme {
        ItemDaLista(item = ItemCompra(texto = "Arroz"))
    }
}

@Preview
@Composable
private fun IconePreview() {
    SuperComprasTheme {
        Icone(icone = Icons.Default.Delete)
    }
}


data class ItemCompra(
    val texto: String,
    var foiComprado: Boolean = false,
)