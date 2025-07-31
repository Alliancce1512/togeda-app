package com.togeda.app.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTextField(
    value                   : String,
    onValueChange           : (String) -> Unit,
    label                   : String,
    leadingIcon             : @Composable (() -> Unit)? = null,
    trailingIcon            : @Composable (() -> Unit)? = null,
    visualTransformation    : VisualTransformation = VisualTransformation.None,
    keyboardOptions         : KeyboardOptions = KeyboardOptions.Default,
    isError                 : Boolean = false,
    modifier                : Modifier = Modifier,
    enabled                 : Boolean = true,
    singleLine              : Boolean = true,
    shape                   : RoundedCornerShape = RoundedCornerShape(16.dp),
    containerColor          : Color = MaterialTheme.colorScheme.surface,
    textColor               : Color = MaterialTheme.colorScheme.onSurface,
    labelColor              : Color = MaterialTheme.colorScheme.tertiary
) {
    OutlinedTextField(
        modifier                = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        value                   = value,
        onValueChange           = onValueChange,
        label                   = { Text(label, color = labelColor) },
        leadingIcon             = leadingIcon,
        trailingIcon            = trailingIcon,
        visualTransformation    = visualTransformation,
        keyboardOptions         = keyboardOptions,
        isError                 = isError,
        enabled                 = enabled,
        singleLine              = singleLine,
        shape                   = shape,
        colors                  = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor  = containerColor,
            focusedTextColor        = textColor,
            unfocusedTextColor      = textColor,
            disabledTextColor       = labelColor,
            focusedLabelColor       = labelColor,
            unfocusedLabelColor     = labelColor,
            cursorColor             = textColor
        )
    )
}