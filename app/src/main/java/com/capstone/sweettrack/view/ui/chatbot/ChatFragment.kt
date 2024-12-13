package com.capstone.sweettrack.view.ui.chatbot

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.sweettrack.adapter.ChatHistoryAdapter
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentChatBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historyLayoutManager = LinearLayoutManager(context)
        binding.rvChatHistory.layoutManager = historyLayoutManager

        val chatAdapter = ChatHistoryAdapter()
        binding.rvChatHistory.adapter = chatAdapter

        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        chatViewModel.chatHistory.observe(viewLifecycleOwner) { messages ->
            chatAdapter.setChatHistory(messages)
            if (chatAdapter.itemCount > 0) {
                binding.rvChatHistory.smoothScrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        chatViewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }

        binding.btnSend.setOnClickListener {
            val input = binding.tietInputTextEditText.text.toString()
            if (input.isNotEmpty()) {
                chatViewModel.sendMessage(input)
                binding.tietInputTextEditText.text?.clear()
                hideKeyboard(it)
            } else {
                Toast.makeText(requireContext(), "Input cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        toggleBottomNavVisibility(View.GONE)
    }

    override fun onStop() {
        super.onStop()
        toggleBottomNavVisibility(View.VISIBLE)
    }

    private fun toggleBottomNavVisibility(visibility: Int) {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = visibility
    }

    private fun hideKeyboard(view: View) {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
