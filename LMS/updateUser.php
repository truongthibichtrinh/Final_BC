<?php
	require_once'class.php';
	if(ISSET($_POST['update'])){
		$db=new db_class();
		$user_id=$_POST['user_id'];
		$username=$_POST['username'];
		$password=$_POST['password'];
		$name=$_POST['name'];
		$role=$_POST['role'];
		$db->update_user($user_id, $username, $password, $name, $role);
		echo"<script>alert('Update user successfully')</script>";
		echo"<script>window.location='user.php'</script>";
	}
?>