<?php
ini_set('display_errors', '1');
error_reporting(E_ALL);


		// Connect to the Database
		$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450btm3';
        $username = '_450btm3';
        $password = 'Ruxyut';

		try {

        	$db = new PDO($dsn, $username, $password);
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	        $column = isset($_GET['column']) ? $_GET['column'] : '';
	        $email = isset($_GET['email']) ? $_GET['email'] : '';
	        $beverageID = isset($_GET['beverageID']) ? $_GET['beverageID'] : '';
	        $newData = isset($_GET['newData']) ? $_GET['newData'] : '';

	        if ($column == 'description' || $column == 'imageadd' || $column == 'rate') {
	        	if ($column == 'rate' && !is_numeric($newData)) {
	       			echo '{"result": "fail", "error": "is not an integer. "}';
	        	} else {
	        		//build query
	                $sql = "UPDATE Cellar SET $column = '" . $newData . "'";
	                $sql .= "WHERE email = '" . $email . "'and id = '".$beverageID."'";
	             
	                //attempts to add record
	                if ($db->query($sql)) {
	                    echo '{"result": "success"}';
	                    $db = null;
	                }
            	}
	        } else {
	        	echo '{"result": "fail", "error": not the correct column."}';
	        }
	    } catch(PDOException $e) {
                if ((int)($e->getCode()) == 23000) {
                    echo '{"result": "fail", "error": "That email address has already been registered."}';
                } else {
                    echo 'Error Number: ' . $e->getCode() . '<br>';
                    echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
                }
        }



?>

