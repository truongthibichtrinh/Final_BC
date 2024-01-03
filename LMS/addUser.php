<?php
	require_once'class.php';
	if(ISSET($_POST['confirm'])){
		$db=new db_class();
		$username=$_POST['username'];
		$password=$_POST['password'];
		$name=$_POST['name'];
		$role=$_POST['role'];
		$db->add_user($username,$password,$name,$role);
		echo"<script>alert('User added successfully')</script>";
		echo"<script>window.location='user.php'</script>";
	}
?>