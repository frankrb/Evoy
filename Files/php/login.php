<?php

$user = $_POST["user"];
$password = $_POST["password"];

$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xframos001"; #el usuario para esa base de datos
$DB_PASS="ESA2IrN7"; #la clave para ese usuario
$DB_DATABASE="Xframos001_evoy"; #la base de datos a la que hay que conectarse

# Se establece la conexión:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

#Comprobamos conexión
if (mysqli_connect_errno($con)) {
echo 'Error de conexion: ' . mysqli_connect_error();
exit();
}else{
	#lo que queramos hacer 

	# Ejecutar la sentencia SQL
	$resultado = mysqli_query($con, "SELECT * FROM Users WHERE user = '$user' AND password = '$password'");
	
	# Comprobar si se ha ejecutado correctamente
	if (!$resultado) {
	echo 'Ha ocurrido algún error: ' . mysqli_error($con);
	}else{
	#accedemos a los valores del resultado
	
	#Acceder al resultado
	$fila = mysqli_fetch_row($resultado);
	
	# Generar el array con los resultados con la forma Atributo - Valor
	$arrayresultados = array('usuario' => $fila[0],'nombre' => $fila[1],'apellidos' => $fila[2],'email' => $fila[3],'contra'=>$fila[4],'fecha_nacimiento'=>$fila[5]);
	
	#Devolver el resultado en formato JSON
	echo json_encode($arrayresultados);
	}
}
?>