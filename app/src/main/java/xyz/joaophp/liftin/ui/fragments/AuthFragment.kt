package xyz.joaophp.liftin.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel

class AuthFragment : Fragment() {

    private val appViewModel: AppViewModel by activityViewModels()
}