package com.example.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.Agreement;
import com.example.entity.Comment;
import com.example.service.AgreementService;
import com.example.service.CollectService;
import com.example.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agreement")
@Api(tags = "点赞接口")
public class AgreementController {

    @Autowired
    private AgreementService agreementService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/agree")
    @ApiOperation("点赞")
    @Transactional
    public R<String> agree(@RequestBody Agreement agreement){
        agreementService.save(agreement);

        Comment com = commentService.getById(agreement.getCommentId());
        com.setAgreement(com.getAgreement()+1);
        commentService.updateById(com);
        return R.success("点赞成功!");
    }

    @DeleteMapping("/unagree")
    @ApiOperation("取消点赞")
    @Transactional
    public R<String> unagree(@RequestBody Agreement agreement){
        LambdaQueryWrapper<Agreement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Agreement::getUserId,agreement.getUserId());
        lambdaQueryWrapper.eq(Agreement::getCommentId,agreement.getCommentId());
        lambdaQueryWrapper.eq(Agreement::getDeleted,0);
        agreementService.remove(lambdaQueryWrapper);

        Comment com = commentService.getById(agreement.getCommentId());
        com.setAgreement(com.getAgreement()-1);
        commentService.updateById(com);
        return R.success("取消点赞成功!");
    }

    @GetMapping("/isAgree")
    @ApiOperation("判断是否点赞")
    public R<String> isAgree(@RequestBody Agreement agreement) {
        LambdaQueryWrapper<Agreement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Agreement::getUserId, agreement.getUserId());
        lambdaQueryWrapper.eq(Agreement::getCommentId, agreement.getCommentId());

        Agreement agreementone = agreementService.getOne(lambdaQueryWrapper);
        if (agreementone == null)
            return R.nferror("未点赞");
        return R.success("已点赞");
    }

}
