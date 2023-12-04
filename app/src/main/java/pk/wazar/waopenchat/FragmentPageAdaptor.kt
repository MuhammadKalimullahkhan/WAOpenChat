package pk.wazar.waopenchat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import pk.wazar.waopenchat.fragments.History
import pk.wazar.waopenchat.fragments.NewChat
import pk.wazar.waopenchat.fragments.Settings

class FragmentPageAdaptor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NewChat()
            1 -> History()
            2 -> Settings()
            else -> { NewChat ()
            }
        }
    }
}