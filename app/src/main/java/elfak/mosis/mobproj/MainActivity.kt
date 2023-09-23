package elfak.mosis.mobproj

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.mobproj.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //textView = findViewById(R.id.textview_first)
        auth=FirebaseAuth.getInstance()
        user = auth.currentUser!!;
        if (user == null){
            val intent : Intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            //textView.setText(user.email)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.EditFragment || destination.id == R.id.ViewFragment)
                binding.fab.hide()
            else
                binding.fab.show()
        }
        binding.fab.setOnClickListener { view ->
            if(navController.currentDestination?.id == R.id.HomeFragment)
                navController.navigate(R.id.action_HomeFragment_to_EditFragment)
            else if (navController.currentDestination?.id == R.id.ListFragment)
                navController.navigate(R.id.action_ListFragment_to_EditFragment)
            else if (navController.currentDestination?.id == R.id.MapFragment)
                navController.navigate(R.id.action_MapFragment_to_EditFragment)
            else if (navController.currentDestination?.id == R.id.PosaoViewFragment)
                navController.navigate(R.id.action_PosaoViewFragment_to_EditFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_show_map->{
                if(navController.currentDestination?.id == R.id.HomeFragment)
                    navController.navigate(R.id.action_HomeFragment_to_MapFragment)
                else if(navController.currentDestination?.id == R.id.ListFragment)
                    navController.navigate(R.id.action_ListFragment_to_MapFragment)
            }
            R.id.action_show_map -> findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_HomeFragment_to_MapFragment)
            R.id.action_list -> Toast.makeText(this, "List", Toast.LENGTH_SHORT).show()
            R.id.action_edit -> Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
            R.id.action_posaolist ->Toast.makeText(this, "UserList", Toast.LENGTH_SHORT).show()
            R.id.action_logout -> Firebase.auth.signOut()
        }
            return super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}