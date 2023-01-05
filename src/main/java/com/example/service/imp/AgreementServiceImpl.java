package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Agreement;
import com.example.entity.User;
import com.example.mapper.AgreementMapper;
import com.example.mapper.UserMapper;
import com.example.service.AgreementService;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AgreementServiceImpl extends ServiceImpl<AgreementMapper, Agreement> implements AgreementService {
}
