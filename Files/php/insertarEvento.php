<?php

$iduser = $_POST["iduser"];
$name = $_POST["name"];
$details = $_POST["details"];
$location = $_POST["location"];
$event_date = $_POST["event_date"];
$image = $_POST["image"];

$resp = "";

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
	$resultado = mysqli_query($con, "INSERT INTO Events (name,details, location,event_date,iduser,image) VALUES ('$name','$details','$location','$event_date','$iduser','$image')");
	
	# Comprobar si se ha ejecutado correctamente
	if (!$resultado) {
	$resp = 'Ha ocurrido algún error: ' . mysqli_error($con);
	}else{
	
	#depuramos errores
	$stmt = mysqli_prepare($con,$resultado);
	mysqli_stmt_bind_param($stmt,"sss",$titulo,$foto,$usuario);
	mysqli_stmt_execute($stmt);
	if (mysqli_stmt_errno($stmt)!=0) {
	$resp= 'Error de sentencia: ' . mysqli_stmt_error($stmt);
	}else{
		$resp ="correcto";
	}
	
	
	}
	# Generar el array con los resultados con la forma Atributo - Valor
	$arrayresultados = array('respuesta' => $resp);
	
	#Devolver el resultado en formato JSON
	echo json_encode($arrayresultados);
}
?>