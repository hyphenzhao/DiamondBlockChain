<?php
	$new_ip = (string) $_GET['ip'];
	$ip_exist = false;
	$link = new mysqli("localhost","root","123456","utsfinal");
	if(mysqli_connect_error()) 
	{
		echo "Connection failed.";
	}
	$sql = "select ip from iplist;";
	$result = $link->query($sql);
	if($result->num_rows > 0)
	{
		while($row = $result->fetch_assoc()) 
		{
			if($new_ip == $row['ip'])
				$ip_exist = true;
			echo $row['ip']."\n";
		}
	}
	if($new_ip != "" && !$ip_exist) 
	{
		$sql = "insert into iplist values(null,'{$new_ip}');";
		$link->query($sql);
	}
?>
