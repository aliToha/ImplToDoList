package com.muthohhari.todolist.ui


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.muthohhari.todolist.*

import com.muthohhari.todolist.model.Todo
import com.muthohhari.todolist.presenter.TodoListPresenter
import kotlinx.android.synthetic.main.fragment_todo_list.*

class TodoList : Fragment(), ViewTodoList {
    private var openFragment: OpenFragment? = null
    private var list: MutableList<Todo> = mutableListOf()
    private lateinit var presenter: TodoListPresenter
    private lateinit var adapter: AdapterList
    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var googleSignInClient: GoogleSignInClient
    private var user: String? = " "

    fun setOpenFragment(openFragment: OpenFragment) {
        this.openFragment = openFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context!!, gso)
        val bundle = arguments
        user = bundle?.getString("USER")
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        header.text = "Hi $user !"
        swipe.setOnRefreshListener {
            list.clear()
            presenter.listActivity(user!!)
        }

        list_item.hasFixedSize()
        list_item.layoutManager = LinearLayoutManager(context)
        adapter = AdapterList(context!!, list) { partItem: Todo -> itemClick(partItem) }
        list_item.adapter = adapter

        presenter = TodoListPresenter(this, context!!)
        presenter.listActivity(user!!)

        fav.setOnClickListener {
            showDialogInput()
        }

        bottomSheet = BottomSheetBehavior.from(bottom_sheet)
        bottomSheet.isHideable = true
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun refresh() {
        presenter.listActivity(user!!)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        openFragment?.openFragment(R.string.login)
        googleSignInClient.signOut()
        FirebaseAuth.getInstance().signOut()
    }

    private fun showDialogInput() {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_custom)
        dialog.setTitle("Add Activity")
        val inputActivity = dialog.findViewById<TextInputLayout>(R.id.inputActivity)
        val inputDesc = dialog.findViewById<TextInputLayout>(R.id.inputDesc)
        val save = dialog.findViewById<Button>(R.id.btn_success)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancle)
        val activity = dialog.findViewById<EditText>(R.id.activity)
        val description = dialog.findViewById<EditText>(R.id.description)

        save.setOnClickListener {
            val i = activity?.text.toString()
            val desc = description?.text.toString()
            if(i.isEmpty()) {
                inputActivity.helperText = "Activity should not empty"
            }else if(desc.isEmpty()) {
                inputDesc.helperText = "Description should not empty"
            }else {
                dialog.dismiss()
                presenter.saveActivity(i, user!!, desc)
            }
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun itemClick(partItem: Todo) {
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        description.text = partItem.description
    }

    override fun showLoading() {
        progress_bar.visible()
        list_item.invisible()
    }

    override fun hideLoading() {
        progress_bar.invisible()
        list_item.visible()
    }

    override fun empty() {
        swipe.isRefreshing = false
        progress_bar.invisible()
        list_item.invisible()
    }

    override fun showList(data: List<Todo>) {
        swipe.isRefreshing = false
        list.clear()
        list.addAll(data)
        adapter.notifyDataSetChanged()
    }

}
