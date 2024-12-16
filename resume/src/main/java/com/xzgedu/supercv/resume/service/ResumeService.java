package com.xzgedu.supercv.resume.service;

import com.xzgedu.supercv.common.exception.ResumeTemplateNotFoundException;
import com.xzgedu.supercv.resume.domain.*;
import com.xzgedu.supercv.resume.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private ResumeBaseInfoRepo resumeBaseInfoRepo;

    @Autowired
    private ResumeBaseInfoItemRepo resumeBaseInfoItemRepo;

    @Autowired
    private ResumeModuleRepo resumeModuleRepo;

    @Autowired
    private ResumeModuleItemRepo resumeModuleItemRepo;

    @Autowired
    private ResumeTemplateRepo resumeTemplateRepo;

    public Resume getResumeById(long id) {
        return resumeRepo.getResumeById(id);
    }

    public List<Resume> getResumesPagination(int limitOffset, int limitSize) {
        return resumeRepo.getResumesPagination(limitOffset, limitSize);
    }

    public int countResumes() {
        return resumeRepo.countResumes();
    }

    public List<Resume> getResumesByUid(long uid, int limitOffset, int limitSize) {
        return resumeRepo.getResumesByUid(uid, limitOffset, limitSize);
    }

    public int countResumesByUid(long uid) {
        return resumeRepo.countResumesByUid(uid);
    }

    public boolean addResume(Resume resume) {
        return resumeRepo.addResume(resume);
    }

    public boolean deleteResume(long id) {
        return resumeRepo.deleteResume(id);
    }

    /**
     * 改变简历标题、模板、主题颜色、间距等等
     */
    public boolean updateResume(Resume resume) {
        return resumeRepo.updateResume(resume);
    }


    /**
     * 根据模板，创建一个简历，简历中包含demo resume的所有内容
     */
    public Resume createResumeFromTemplateDemo(long uid, long templateId) throws ResumeTemplateNotFoundException {
        ResumeTemplate template = resumeTemplateRepo.getTemplateById(templateId);
        if (template == null || template.getDemoResumeId() == null) {
            throw new ResumeTemplateNotFoundException("Failed to find resume template: " + templateId);
        }
        return copyResume(uid, template.getDemoResumeId());
    }

    /**
     * 根据模板，以及用户上传的简历文件，创建一个简历
     */
    public Resume createResumeFromExistingFile(long uid, long templateId, String resumeFileUrl)
            throws ResumeTemplateNotFoundException {
        ResumeTemplate template = resumeTemplateRepo.getTemplateById(templateId);
        if (template == null || template.getDemoResumeId() == null) {
            throw new ResumeTemplateNotFoundException("Failed to find resume template: " + templateId);
        }

        //TODO：从示例简历直接将specific setting拷贝过来，然后创建default modules，
        // 解析文件，将解析后的内容存到对应的modules，没有对应的，新建module

        return null;
    }

    /**
     * 拷贝简历
     */
    public Resume copyResume(long uid, long resumeId) {
        //TODO: 复制一份简历，并返回
        return null;
    }

    /**
     * 查询完整的简历
     */
    public Resume getResumeDetail(long uid, long id) {
        Resume resume = resumeRepo.getResumeById(id);

        // fill base info
        ResumeBaseInfo baseInfo = resumeBaseInfoRepo.getResumeBaseInfoByResumeId(resume.getId());
        List<ResumeBaseInfoItem> baseInfoItems = resumeBaseInfoItemRepo.getResumeBaseInfoItems(baseInfo.getId());
        baseInfo.getItems().addAll(baseInfoItems);
        resume.setBaseInfo(baseInfo);

        // fill modules
        List<ResumeModule> modules = resumeModuleRepo.getResumeModulesByResumeId(resume.getId());
        List<Long> moduleIds = new ArrayList<>();
        for (ResumeModule module : modules) {
            moduleIds.add(module.getId());
        }
        List<ResumeModuleItem> moduleItems = resumeModuleItemRepo.getResumeModuleItemsByModuleIds(moduleIds);
        Map<Long, List<ResumeModuleItem>> moduleItemMap = new HashMap<>();
        for (ResumeModuleItem item : moduleItems) {
            List<ResumeModuleItem> items = moduleItemMap.get(item.getModuleId());
            if (items == null) {
                items = new ArrayList<>();
                moduleItemMap.put(item.getModuleId(), items);
            }
            items.add(item);
        }
        for (ResumeModule module : modules) {
            List<ResumeModuleItem> items = moduleItemMap.get(module.getId());
            if (items != null && !items.isEmpty()) {
                items.sort(new Comparator<ResumeModuleItem>() {
                    @Override
                    public int compare(ResumeModuleItem o1, ResumeModuleItem o2) {
                        return o1.getSortValue() - o2.getSortValue();
                    }
                });
            }
            module.getModuleItems().addAll(items);
        }
        resume.getModules().addAll(modules);

        return resume;
    }
}
