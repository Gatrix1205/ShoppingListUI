package com.example.shoppinglistui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id:Int,
    var name: String,
    var isEditable : Boolean = false,
    var quantity : Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList(){
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var itemName by remember {
        mutableStateOf("")
    }

    var itemQ by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {
            showDialog = true
        }) {
            Text("Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            content = {
                item(sItems) {

                }
            })
        if(showDialog){
            AlertDialog(
                title = {
                        Text("Add your Items")
                },
                text = {
                    Column {
                        OutlinedTextField(value = itemName,
                            label = {
                                    Text("Enter item name")
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onValueChange ={
                            itemName = it
                        } )
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(value = itemQ,
                            label = {
                                    Text("Enter Quantity")
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onValueChange ={
                                itemQ = it
                            } )

                    }
                },
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Button(onClick = {
                                        if(itemName.isNotEmpty()){
                                            val shoppingItem = ShoppingItem(
                                                id = sItems.size+1,
                                                name = itemName,
                                                quantity = itemQ.toInt(),
                                            )
                                            sItems = sItems + shoppingItem
                                            showDialog = false
                                            itemName = ""
                                        }

                                    }) {

                                        Text("Add")
                                    }
                                    Button(onClick = {} ) {
                                        Text("Cancel")
                                    }
                                }
                },
            )
        }

    }
}
