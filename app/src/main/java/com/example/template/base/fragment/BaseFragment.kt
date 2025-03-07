package com.example.template.base.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.template.base.activity.BaseActivity

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!


    open fun setupUI(savedInstanceState: Bundle?) {}
    open fun setupListener() {}
    open fun observerUI() {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // hide system ui
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideNavigationBar()
        } else {
            hideNavigationBarLegacy()
        }
        //
        setupUI(savedInstanceState)
        setupListener()
        observerUI()

    }

    //=========================================== NAVIGATION =======================================
//    open fun navigate(resId: Int) {
//        val navOptionNormal = NavOptions
//            .Builder()
//            .setEnterAnim(R.anim.enter_from_right)
//            .setExitAnim(R.anim.exit_to_left)
//            .build()
//        navController.navigate(resId, null, navOptionNormal)
//    }
//
//    open fun navigateAndRemove(resId: Int) {
//        val navOptions = NavOptions.Builder()
//            .setPopUpTo(
//                navController.graph.startDestinationId,
//                true
//            )
//            .setEnterAnim(R.anim.enter_from_right)
//            .setExitAnim(R.anim.exit_to_left)
//            .build()
//        navController.navigate(resId, null, navOptions)
//    }
//
//    open fun navigateAndReplace(resId: Int) {
//        val navOptions = navController.currentDestination?.let {
//            NavOptions.Builder()
//                .setPopUpTo(
//                    it.id,
//                    true
//                )
//                .setEnterAnim(R.anim.enter_from_right)
//                .setExitAnim(R.anim.exit_to_left)
//                .build()
//        }
//        navController.navigate(resId, null, navOptions)
//    }
//
//    /**
//     * Example:
//     *
//     * `kotlin`
//     *
//     * val data = "data from fragment jhfhgn1"
//     * val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(data)
//     * navigateArgs(action)
//     *
//     *
//     * `nav_graph.xml`
//     *
//     * <fragment
//     * android:id="@+id/SecondFragment"
//     *   android:name="com.example.androidmvvm.ui.fragment.SecondFragment"
//     *   android:label="@string/second_fragment_label"
//     *   tools:layout="@layout/fragment_second">
//     *   <argument
//     *       android:name="testArgs"
//     *       android:defaultValue="defaultValue"
//     *       app:argType="string" />
//     * </fragment>
//     *
//     *
//     *
//     */
//    open fun navigateArgs(action: NavDirections) {
//        navController.navigate(action)
//    }
//
//    open fun popBackStack() {
//        navController.popBackStack()
//    }


    // ======================================== COMMON =============================================
//    private fun hideUI() {
//        val flags = View.SYSTEM_UI_FLAG_LOW_PROFILE or
//                    View.SYSTEM_UI_FLAG_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        activity?.window?.decorView?.systemUiVisibility = flags
//        (activity as? AppCompatActivity)?.supportActionBar?.show()
//    }

    // ============================== HIDE UI ===================================
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideNavigationBar() {
        activity?.window?.insetsController?.apply {
            hide(WindowInsets.Type.navigationBars())
//            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    private fun hideNavigationBarLegacy() {
        activity?.window?.decorView?.systemUiVisibility = (
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                        View.SYSTEM_UI_FLAG_FULLSCREEN or
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }



    protected fun showLoading() {
        val activity = requireActivity()
        if (activity is BaseActivity<*>) {
            activity.showLoadingDialog()
        }
    }

    protected fun hideLoading() {
        val activity = requireActivity()
        if (activity is BaseActivity<*>) {
            activity.hideLoadingDialog()
        }
    }

    // =====================================LIFE CYCLE==============================================
//    override fun onResume() {
//        super.onResume()
//        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//    }

//    override fun onPause() {
//        super.onPause()
//        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}