package com.capstone.sweettrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.sweettrack.view.ui.chatbot.Message
import com.coding.sweettrack.R

class ChatHistoryAdapter : RecyclerView.Adapter<ChatHistoryAdapter.MessageViewHolder>() {

    private val chatHistory = ArrayList<Message>()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val messageItemView =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false) as ViewGroup
        return MessageViewHolder(messageItemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val userMessageText = holder.itemView.findViewById<TextView>(R.id.tv_userMessageText)
        val message = chatHistory[position]
        userMessageText.text = message.text
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatHistory[position].isLocalUser) R.layout.item_message_local else R.layout.item_message_ai
    }

    override fun getItemCount(): Int = chatHistory.size

    fun setChatHistory(messages: List<Message>) {
        chatHistory.clear()
        chatHistory.addAll(messages)
        notifyDataSetChanged()
    }
}