package com.david.redcristianauno.domain

import com.david.redcristianauno.vo.Resource

interface PermissionUseCase {
    suspend fun getPermission(id_user: String): Resource<String>
}