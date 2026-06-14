package top.overlink.read.ui.rss.favorites


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import top.overlink.read.R
import top.overlink.read.base.VMBaseFragment
import top.overlink.read.constant.AppLog
import top.overlink.read.data.appDb
import top.overlink.read.data.entities.RssStar
import top.overlink.read.databinding.FragmentRssArticlesBinding
import top.overlink.read.lib.dialogs.alert
import top.overlink.read.lib.theme.primaryColor
import top.overlink.read.ui.rss.read.ReadRssActivity
import top.overlink.read.ui.widget.recycler.VerticalDivider
import top.overlink.read.utils.applyNavigationBarPadding
import top.overlink.read.utils.setEdgeEffectColor
import top.overlink.read.utils.startActivity
import top.overlink.read.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RssFavoritesFragment() : VMBaseFragment<RssFavoritesViewModel>(R.layout.fragment_rss_articles),
    RssFavoritesAdapter.CallBack {

    constructor(group: String) : this() {
        arguments = Bundle().apply {
            putString("group", group)
        }
    }

    private val binding by viewBinding(FragmentRssArticlesBinding::bind)
    override val viewModel by viewModels<RssFavoritesViewModel>()
    private val adapter: RssFavoritesAdapter by lazy {
        RssFavoritesAdapter(requireContext(), this@RssFavoritesFragment)
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        loadArticles()
    }

    private fun initView() = binding.run {
        refreshLayout.isEnabled = false
        recyclerView.setEdgeEffectColor(primaryColor)
        recyclerView.layoutManager = run {
            recyclerView.addItemDecoration(VerticalDivider(requireContext()))
            LinearLayoutManager(requireContext())
        }
        recyclerView.adapter = adapter
        recyclerView.applyNavigationBarPadding()
    }

    private fun loadArticles() {
        lifecycleScope.launch {
            val group = arguments?.getString("group") ?: "默认分组"
            appDb.rssStarDao.flowByGroup(group).catch {
                AppLog.put("订阅文章界面获取数据失败\n${it.localizedMessage}", it)
            }.flowOn(IO).collect {
                adapter.setItems(it)
            }
        }
    }

    override fun readRss(rssStar: RssStar) {
        startActivity<ReadRssActivity> {
            putExtra("title", rssStar.title)
            putExtra("origin", rssStar.origin)
            putExtra("link", rssStar.link)
        }
    }

    override fun delStar(rssStar: RssStar) {
        alert(R.string.draw) {
            setMessage(getString(R.string.sure_del) + "\n<" + rssStar.title + ">")
            noButton()
            yesButton {
                appDb.rssStarDao.delete(rssStar.origin, rssStar.link)
            }
        }
    }
}
