package com.example.shoppinglistui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



data class ShoppingItem(
    val id: Int,
    var name: String,
    var isEditable: Boolean = false,
    var quantity: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList() {
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
    ) {
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
                items(sItems) {
                    item ->
                    if(item.isEditable){
                        ShoppingEditor(shoppingItem = item, onEditComplete = {
                            editedName, editQ ->
                            sItems = sItems.map {
                                it.copy(isEditable = false)
                            }
                            val editedItem = sItems.find{it.id == item.id}
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editQ
                            }
                        })
                    }else{
                        ShoppingListItems(shoppingItem = item,
                            onEditClick = {
                                sItems = sItems.map {
                                    it.copy(isEditable = it.id == item.id)
                                }
                            }) {
                            sItems = sItems - item
                        }
                    }
                }
            })
        if (showDialog) {
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
                            onValueChange = {
                                itemName = it
                            })
                        Spacer(modifier = Modifier.padding(8.dp))
                        OutlinedTextField(value = itemQ,
                            label = {
                                Text("Enter Quantity")
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onValueChange = {
                                itemQ = it
                            })

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
                    ) {
                        Button(onClick = {
                            if (itemName.isNotEmpty()) {
                                val shoppingItem = ShoppingItem(
                                    id = sItems.size + 1,
                                    name = itemName,
                                    quantity = itemQ.toInt(),
                                )
                                sItems = sItems + shoppingItem
                                showDialog = false
                                itemName = ""
                                itemQ = ""
                            }

                        }) {

                            Text("Add")
                        }
                        Button(onClick = {
                            showDialog = false
                        }) {
                            Text("Cancel")
                        }
                    }
                },
            )
        }

    }
}

@Composable
fun ShoppingListItems(
    shoppingItem: ShoppingItem,
    onEditClick : () -> Unit,
    onDeleteClick: () ->Unit
){
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    2.dp, Color(0xff018786)
                ),
                shape = RoundedCornerShape(8.dp)
            )
    ){
        Text(text = shoppingItem.name,
            modifier = Modifier.padding(8.dp))
        Text(text = "Quantity : ${shoppingItem.quantity}",
            modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            IconButton(onClick = onEditClick) {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = "Edit Icon"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = "Delete Icon"
                )
            }
        }
    }
}

@Composable
fun ShoppingEditor(shoppingItem : ShoppingItem,
                   onEditComplete :(String, Int) -> Unit){
    var editedName by remember {
        mutableStateOf(shoppingItem.name)
    }

    var editedCount by remember {
        mutableStateOf(shoppingItem.quantity.toString())
    }

    var isEditable by remember {
        mutableStateOf(shoppingItem.isEditable)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {
                editedName = it
            } , singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
            BasicTextField(
                value = editedCount,
                onValueChange = {
                    editedCount = it
                } , singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }
        Button(onClick = {
            isEditable = false
            onEditComplete(editedName, editedCount.toIntOrNull() ?: 1)
        }) {
            Text("Save")
        }
    }
}

