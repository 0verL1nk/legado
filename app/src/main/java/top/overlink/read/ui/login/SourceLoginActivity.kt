package top.overlink.read.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import top.overlink.read.R
import top.overlink.read.base.VMBaseActivity
import top.overlink.read.data.entities.BaseSource
import top.overlink.read.databinding.ActivitySourceLoginBinding
import top.overlink.read.utils.showDialogFragment
import top.overlink.read.utils.viewbindingdelegate.viewBinding


class SourceLoginActivity : VMBaseActivity<ActivitySourceLoginBinding, SourceLoginViewModel>() {

    override val binding by viewBinding(ActivitySourceLoginBinding::inflate)
    override val viewModel by viewModels<SourceLoginViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel.initData(intent, success = { source ->
            initView(source)
        }, error = {
            finish()
        })
    }

    private fun initView(source: BaseSource) {
        if (source.loginUi.isNullOrEmpty()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, WebViewLoginFragment(), "webViewLogin")
                .commit()
        } else {
            showDialogFragment<SourceLoginDialog>()
        }
    }

}