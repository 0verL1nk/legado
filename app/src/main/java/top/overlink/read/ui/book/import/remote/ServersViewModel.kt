package top.overlink.read.ui.book.import.remote

import android.app.Application
import top.overlink.read.base.BaseViewModel
import top.overlink.read.data.appDb
import top.overlink.read.data.entities.Server

class ServersViewModel(application: Application): BaseViewModel(application) {


    fun delete(server: Server) {
        execute {
            appDb.serverDao.delete(server)
        }
    }

}