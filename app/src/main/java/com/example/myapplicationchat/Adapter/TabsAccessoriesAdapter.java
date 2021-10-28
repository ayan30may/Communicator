package com.example.myapplicationchat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplicationchat.Fragments.CallsFragment;
import com.example.myapplicationchat.Fragments.ChatsFragment;
import com.example.myapplicationchat.Fragments.ContactsFragment;

public class TabsAccessoriesAdapter extends FragmentPagerAdapter {
    public TabsAccessoriesAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Chats";

            case 1:
                return "Contacts";

            default:
                return null;
        }
    }
}
