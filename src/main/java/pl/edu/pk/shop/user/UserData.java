package pl.edu.pk.shop.user;

import java.util.HashMap;

import pl.edu.pk.shop.database.*;
import pl.edu.pk.shop.tabledata.TableData;
import pl.edu.pk.shop.function.*;
import pl.edu.pk.shop.address.*;

/**Stores user's data.
 * @author Christopher Abram
 */
public class UserData implements TableData {
	// vars {
	
		public int id;
		public String first_name;
		public String second_name;
		public Function function;
		public Address address;
		public String email;
		public String phonenumber;
		
	// } methods {
		
		public UserData(){}
		
		public UserData(int ID){
			if(ID > -1){
				id = ID;
				load();
			}
		}// end UserData
		
		/**Loads all information from SQL Server.
		 * @author Daria G (update)
		 * @param int id - identifier of user.
		 * @return void
		 */
		public boolean load(){
			Database db = Database.getInstance();
			db.connect();
			db.query("SELECT id, first_name, second_name, id_function, id_address, email, phonenumber FROM users WHERE id = ?");
			db.prepare(id);
			if(db.execute()){
				try {
					// update: Daria G
					Results res = db.getResults();
					if(res.hasNext()){
						Results.Row row = res.next();
						id = Integer.parseInt(row.get("id"));
						first_name = row.get("first_name");
						second_name = row.get("second_name");
						function = new Function(Integer.parseInt(row.get("id_function"))); //?
						int id_address = Integer.parseInt(row.get("id_address"));
						address = new Address(id_address);
						email = row.get("email");
						phonenumber = row.get("phonenumber");
						
						return true;
					}
					
				} catch(DatabaseException dbe){
					System.out.println("Error: " + dbe.getMessage());
				}
			}
			return false;
		}// end load
		
		/**Updates all information in SQL Server using UPDATE query.
		 * @author Daria G
		 * @return boolean - true if update properly, false otherwise.
		 */
		public boolean update(){
			
			try{
				Database db = Database.getInstance();
				db.connect();
				db.query("UPDATE users SET first_name = ?, second_name = ?, id_function = ?, id_addres = ?, email = ?, phonenumber = ? WHERE id = ?");
				db.prepare(first_name, second_name, function.getId(), address.getId(), email, phonenumber, id);
				if(db.execute())
					return true;
			} catch(DatabaseException dbe) {
				System.out.println("Error: " + dbe.getMessage());
			}
				return false;
		}// end update
		
		
		/**Inserts row information to SQL Server using INSERT query.
		 * @author Daria G
		 * @return boolean - true if insert properly, false otherwise.
		 */
		public boolean insert(){
			try{
				Database db = Database.getInstance();
				db.connect();
				db.query("INSERT INTO users(id, first_name, second_name, id_function, id_address, email, phonenumber) VALUES (?, ?, ?, ?, ?, ?, ?)");
				db.prepare(id, first_name, second_name, function.getId(), address.getId(), email, phonenumber);
				if(db.execute())
					return true;
			} catch(DatabaseException dbe) {
				System.out.println("Error: " + dbe.getMessage());
			}
				return false;
			
		}// end insert
		
		/**Delete row in table using DELETE query.
		 * @author Daria G
		 * @return boolean - true if delete properly, false otherwise.
		 */
		public boolean delete(){
			try{
				Database db = Database.getInstance();
				db.connect();
				db.query("DELETE FROM users WHERE id = ?");
				db.prepare(id);
				if(db.execute())
					return true;
			} catch(DatabaseException dbe) {
				System.out.println("Error: " + dbe.getMessage());
			}
				return false;
		}// end delete
		
		/**Gets all UserData properties as HashMap.
		 * Keys name are the same as properties names.
		 * @author Daria G
		 * @return HashMap<String, Object> - UserData as HashMap
		 * @see java.util.HashMap
		 */
		public HashMap<String, Object> getAsHashMap(){
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", id);
			map.put("first_name", first_name);
			map.put("second_name", second_name);
			map.put("id_function", function.getId()); 
			map.put("id_address", address.getId());
			map.put("email", email);
			map.put("phonenumber", phonenumber);
			
			return map;
		}// end getAsHashMap
		
	// }
}
